/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.hsbremen.tc.tnc.im.example.test;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueTesting;
import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentTypeEnum;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
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
 *
 */
public class TestImvEvaluationUnit extends AbstractImEvaluationUnitIetf
        implements ImvEvaluationUnit {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TestImvEvaluationUnit.class);

    public static final long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
    public static final long TYPE = PaComponentTypeEnum.IETF_PA_TESTING.id();

    private final String value;

    private ImvRecommendationPair recommendation;

    /**
     * Create a the evaluation unit with the given handshake retry listener
     * and given path to a file containing reference values for the evaluation
     * unit.
     *
     * @param evaluationValuesFile the reference value file
     * @param globalHandshakeRetryListener the global handshake retry listener.
     */
    public TestImvEvaluationUnit(final String value,
            final GlobalHandshakeRetryListener globalHandshakeRetryListener) {
        super(globalHandshakeRetryListener);

        this.value = (value != null) ? value : "";

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

        if(LOGGER.isDebugEnabled()){
            StringBuilder b =
                    new StringBuilder("Value Test:\n")
            .append("Property\t|Expected\t|Actual\t|\n")
            .append("Name\t|")
            .append(this.value.trim())
            .append("\t|")
            .append(value.getContent().trim())
            .append("\t|\n");
            LOGGER.debug(b.toString());
        }

        if (value.getContent().trim()
                .equals(this.value.trim())) {
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
        // TODO implement error handling
        StringBuilder b = new StringBuilder();
        b.append("An error was received: \n")
                .append("Error with vendor ID ")
                .append(value.getErrorVendorId())
                .append(" and type ")
                .append(PaAttributeErrorCodeEnum.fromCode(
                        value.getErrorCode()))
                .append(".\n")
                .append("Error was found in message ")
                .append(value.getErrorInformation().getMessageHeader()
                        .toString());

        if (value.getErrorInformation() instanceof
                PaAttributeValueErrorInformationInvalidParam) {
            b.append(" at offset ").append(
                    ((PaAttributeValueErrorInformationInvalidParam) value
                            .getErrorInformation()).getOffset());
        }

    }

    @Override
    public synchronized ImvRecommendationPair getRecommendation(
            final ImSessionContext context) {
        // look if recommendation is present and handle it if possible else
        // return default no recommendation
        ImvRecommendationPair rec = this.recommendation;
        return (rec != null) ? rec : ImvRecommendationPairFactory
                .getDefaultRecommendationPair();
    }

    @Override
    public boolean hasRecommendation(ImSessionContext context) {
        return (this.recommendation != null);
    }

    @Override
    public synchronized List<ImAttribute> lastCall(
            final ImSessionContext context) {

        LOGGER.info("Last call received.");
        return new ArrayList<>(0);
    }
    
    @Override
    public void notifyConnectionChange(ImSessionContext context) {
        LOGGER.debug(new StringBuilder()
            .append("Connection change notification received. ")
            .append("Any existing recommendation will be reset.")
            .toString());
        if (this.recommendation != null){
            this.recommendation = null; // remove recommendation
        }
        
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
                .createAttributeRequest(new AttributeReferenceEntry(
                        IETFConstants.IETF_PEN_VENDORID,
                        PaAttributeTypeEnum.IETF_PA_TESTING.id()));
    }

}
