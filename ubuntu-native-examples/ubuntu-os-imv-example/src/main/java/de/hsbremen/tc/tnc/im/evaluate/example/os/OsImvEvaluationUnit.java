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
package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;
import org.ietf.nea.pa.attribute.PaAttributeValueProductInformation;
import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;
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
import de.hsbremen.tc.tnc.util.ConfigurationPropertiesLoader;

/**
 * Example operating system IMV evaluation unit, which uses
 * a property file to obtain reference values for an
 * evaluation.
 *
 *
 */
public class OsImvEvaluationUnit extends AbstractImEvaluationUnitIetf implements
        ImvEvaluationUnit {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(OsImvEvaluationUnit.class);

    public static final long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
    public static final long TYPE = PaComponentTypeEnum.IETF_PA_OPERATING_SYSTEM
            .id();

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
    public OsImvEvaluationUnit(final String evaluationValuesFile,
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
                if (value
                        instanceof PaAttributeValueStringVersion) {
                    rating += this.handleStringVersion(
                            (PaAttributeValueStringVersion) value, context);

                } else if (value
                        instanceof PaAttributeValueNumericVersion) {
                    rating += this.handleNumericVersion(
                            (PaAttributeValueNumericVersion) value, context);

                } else if (value
                        instanceof PaAttributeValueProductInformation) {
                    rating += this
                            .handleProductInformationVersion(
                                    (PaAttributeValueProductInformation) value,
                                    context);
                }
                if (value instanceof PaAttributeValueError) {
                    this.handleError((PaAttributeValueError) value, context);
                }
            }

            LOGGER.debug("Rating: " + rating + " of 10.");

            final int ratingThreshold = 8;

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
                        .add(PaAttributeFactoryIetf.createAssessmentResult((
                                this.recommendation.getResult()
                                .equals(ImvEvaluationResultEnum
                                .TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR))
                                ? PaAttributeAssessmentResultEnum.
                                        SIGNIFICANT_DIFFERENCES
                                : PaAttributeAssessmentResultEnum.COMPLIANT));
            } catch (ValidationException e) {
                LOGGER.error("Assessment result clould not be created.", e);
            }
        }

        return attributes;
    }

    /**
     * Handles a message attribute containing a string representation of
     * an operating system version.
     *
     * @param value the message attribute
     * @param context the context, which holds connection specific values
     * @return a rating value
     */
    private int handleStringVersion(final PaAttributeValueStringVersion value,
            final ImSessionContext context) {

        int i = 0;
        if (value.getBuildVersion().trim()
                .equals(properties.getProperty("str_build_number").trim())) {
            i++;
        }

        if (value.getVersionNumber().trim()
                .equals(properties.getProperty("str_version_number").trim())) {
            i++;
        }

        if (value.getConfigurationVersion().trim()
                .equals(properties.getProperty("str_config_number").trim())) {
            i++;
        }

        if(LOGGER.isDebugEnabled()){
            StringBuilder b =
                    new StringBuilder("String Version:\n")
            .append("Property\t|Expected\t|Actual\t|\n")
            .append("Build\t|")
            .append(properties.getProperty("str_build_number").trim())
            .append("\t|")
            .append(value.getBuildVersion().trim())
            .append("\t|\n")
            .append("Version\t|")
            .append(properties.getProperty("str_version_number").trim())
            .append("\t|")
            .append(value.getVersionNumber().trim())
            .append("\t|\n")
            .append("Config\t|")
            .append(properties.getProperty("str_config_number").trim())
            .append("\t|")
            .append(value.getConfigurationVersion().trim())
            .append("\t|\n");
            LOGGER.debug(b.toString());
        }

        return i;
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

    /**
     * Handles a message attribute containing product information of
     * an operating system.
     *
     * @param value the message attribute
     * @param context the context, which holds connection specific values
     * @return a rating value
     */
    private int handleProductInformationVersion(
            final PaAttributeValueProductInformation value,
            final ImSessionContext context) {
        int i = 0;

        if (value.getName().contains(properties.getProperty("pi_name"))) {
            i += 2;
        }

        if (value.getProductId() == Integer.parseInt(properties
                .getProperty("pi_id"))) {
            i++;
        }

        if (value.getVendorId() == Integer.parseInt(properties
                .getProperty("pi_vid"))) {
            i++;
        }

        if(LOGGER.isDebugEnabled()){
            StringBuilder b =
                    new StringBuilder("Product Info:\n")
            .append("Property\t|Expected\t|Actual\t|\n")
            .append("Name\t|")
            .append(properties.getProperty("pi_name"))
            .append("\t|")
            .append(value.getName())
            .append("\t|\n")
            .append("Product ID\t|")
            .append(Integer.parseInt(properties.getProperty("pi_id")))
            .append("\t|")
            .append(value.getProductId())
            .append("\t|\n")
            .append("Vendor ID\t|")
            .append(Integer.parseInt(properties.getProperty("pi_vid")))
            .append("\t|")
            .append(value.getVendorId())
            .append("\t|\n");
            LOGGER.debug(b.toString());
        }

        return i;

    }

    /**
     * Handles a message attribute containing a numeric representation of
     * an operating system version.
     *
     * @param value the message attribute
     * @param context the context, which holds connection specific values
     * @return a rating value
     */
    private int handleNumericVersion(
            final PaAttributeValueNumericVersion value,
            final ImSessionContext context) {
        int i = 0;

        if (value.getMajorVersion() == Long.parseLong(properties
                .getProperty("nv_major"))) {
            i++;
        }

        if (value.getMinorVersion() == Long.parseLong(properties
                .getProperty("nv_minor"))) {
            i++;
        }

        if (value.getBuildVersion() == Long.parseLong(properties
                .getProperty("nv_build"))) {
            i++;
        }

        // the rest is not relevant right now

        if(LOGGER.isDebugEnabled()){
            StringBuilder b =
                    new StringBuilder("Numeric Version:\n")
            .append("Property\t|Expected\t|Actual\t|\n")
            .append("Major\t|")
            .append(Long.parseLong(properties
                    .getProperty("nv_major")))
            .append("\t|")
            .append(value.getMajorVersion())
            .append("\t|\n")
            .append("Minor\t|")
            .append(Long.parseLong(properties
                    .getProperty("nv_minor")))
            .append("\t|")
            .append(value.getMinorVersion())
            .append("\t|\n")
            .append("Build\t|")
            .append(Long.parseLong(properties
                    .getProperty("nv_build")))
            .append("\t|")
            .append(value.getBuildVersion())
            .append("\t|\n");
            LOGGER.debug(b.toString());
        }

        return i;
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

        return PaAttributeFactoryIetf.createAttributeRequest(
                new AttributeReferenceEntry(IETFConstants.IETF_PEN_VENDORID,
                        PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION
                                .id()),
                new AttributeReferenceEntry(IETFConstants.IETF_PEN_VENDORID,
                        PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION
                                .id()),
                new AttributeReferenceEntry(IETFConstants.IETF_PEN_VENDORID,
                        PaAttributeTypeEnum.IETF_PA_STRING_VERSION
                                .id()));

    }

}
