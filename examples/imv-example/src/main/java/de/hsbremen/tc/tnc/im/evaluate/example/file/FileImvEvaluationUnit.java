/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Carl-Heinz Genzel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package de.hsbremen.tc.tnc.im.evaluate.example.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueTesting;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.enums.PaComponentTypeEnum;
import de.hsbremen.tc.tnc.im.evaluate.simple.util.ConfigurationPropertiesLoader;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValue;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;

/**
 * Example file IMV evaluation unit, which uses
 * a property file to obtain reference values for an
 * evaluation.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class FileImvEvaluationUnit extends AbstractImEvaluationUnitIetf
        implements ImvEvaluationUnit {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileImvEvaluationUnit.class);

    public static final long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
    public static final long TYPE = PaComponentTypeEnum.IETF_PA_TESTING.id();

    private Properties properties;

    private ImvRecommendationPair recommendation;

    /**
     * Create a the evaluation unit with the given handshake retry listener
     * and given path to a file containing reference values for the evaluation
     * unit.
     *
     * @param evaluationValuesFile the reference value file
     * @param globalHandshakeRetryListener the global handshake retry listener.
     */
    public FileImvEvaluationUnit(final String evaluationValuesFile,
            final GlobalHandshakeRetryListener globalHandshakeRetryListener) {
        super(globalHandshakeRetryListener);

        try {
            properties = ConfigurationPropertiesLoader
                    .loadProperties(evaluationValuesFile, this.getClass());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            properties = null;
        }

    }

    @Override
    public long getVendorId() {
        return VENDOR_ID;
    }

    @Override
    public long getType() {
        return TYPE;
    }

    @Override
    public synchronized List<ImAttribute> evaluate(
            final ImSessionContext context) {
        this.recommendation = null;
        List<ImAttribute> attributes = new ArrayList<>();
        try {
            PaAttribute attrReq = this.getAttributeRequest();
            attributes.add(attrReq);
        } catch (ValidationException e) {
            LOGGER.error("Attribute request clould not be created.", e);
        }

        return attributes;
    }

    @Override
    public synchronized List<ImAttribute> handle(
            final List<? extends ImAttribute> attribute,
            final ImSessionContext context) {
        List<ImAttribute> attributes = new ArrayList<>();
        this.recommendation = null;
        if (properties != null) {
            int rating = 0;
            for (ImAttribute imAttribute : attribute) {
                ImAttributeValue value = imAttribute.getValue();
                if (value instanceof PaAttributeValueTesting) {
                    rating = this.handleTesting(
                            (PaAttributeValueTesting) value, context);
                }
                if (value instanceof PaAttributeValueError) {
                    this.handleError((PaAttributeValueError) value, context);
                }
            }

            LOGGER.debug("Rating: " + rating + " of 1.");

            final int ratingThreshold = 1;

            if (rating < ratingThreshold) {
                this.recommendation = ImvRecommendationPairFactory
                        .createRecommendationPair(
                                ImvActionRecommendationEnum
                                .TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS,
                                ImvEvaluationResultEnum
                                .TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR);
            } else {
                this.recommendation = ImvRecommendationPairFactory
                        .createRecommendationPair(
                                ImvActionRecommendationEnum
                                .TNC_IMV_ACTION_RECOMMENDATION_ALLOW,
                                ImvEvaluationResultEnum
                                .TNC_IMV_EVALUATION_RESULT_COMPLIANT);
            }

            try {
                attributes
                        .add(PaAttributeFactoryIetf.createAssessmentResult(
                                (this.recommendation.getResult().equals(
                                ImvEvaluationResultEnum
                                .TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR))
                                ? PaAttributeAssessmentResultEnum
                                        .SIGNIFICANT_DIFFERENCES
                                : PaAttributeAssessmentResultEnum.COMPLIANT));
            } catch (ValidationException e) {
                LOGGER.error("Assessment result clould not be created.", e);
            }
        }

        return attributes;
    }

    /**
     * Handles a message attribute containing the hash of the file for testing.
     *
     * @param value the message attribute
     * @param context the context, which holds connection specific values
     * @return a rating value
     */
    private int handleTesting(final PaAttributeValueTesting value,
            final ImSessionContext context) {

        if (value.getContent().trim()
                .equals(properties.getProperty("checksum").trim())) {
            return 1;
        }

        return 0;
    }

    /**
     * Handles a message attribute containing an error.
     *
     * @param value the message attribute
     * @param context the context, which holds connection specific values
     */
    private void handleError(final PaAttributeValueError value,
            final ImSessionContext context) {
        // TODO better error handling
        LOGGER.error("IMC has send an error: "
                + value.getErrorInformation().toString());

    }

    @Override
    public synchronized ImvRecommendationPair getRecommendation(
            final ImSessionContext context) {
        // look if recommendation is present and handle it if possible else
        // return default no recommendation
        ImvRecommendationPair rec = this.recommendation;
        this.recommendation = null; // remove recommendation after it has been
                                    // ask for, to make room for a new
                                    // evaluation.
        return (rec != null) ? rec : ImvRecommendationPairFactory
                .getDefaultRecommendationPair();
    }

    @Override
    public boolean hasRecommendation() {
        return (this.recommendation != null);
    }

    @Override
    public synchronized List<ImAttribute> lastCall(
            final ImSessionContext context) {

        LOGGER.info("Last call received.");
        return new ArrayList<>(0);
    }

    @Override
    public void terminate() {
        LOGGER.debug("Terminate called.");
    }

    /**
     * Creates an attribute request attribute to request all
     * necessary attributes of an IMC.
     *
     * @return the message attribute
     * @throws ValidationException if attribute creation fails
     */
    private PaAttribute getAttributeRequest() throws ValidationException {

        return PaAttributeFactoryIetf
                .createAttributeRequest(new AttributeReference(
                        IETFConstants.IETF_PEN_VENDORID,
                        PaAttributeTypeEnum.IETF_PA_TESTING.attributeType()));
    }

}
