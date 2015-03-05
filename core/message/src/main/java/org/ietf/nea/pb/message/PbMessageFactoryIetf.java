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
package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Factory utility to create a IETF RFC 5793 compliant TNCCS message.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class PbMessageFactoryIetf {

    private static final long VENDORID = IETFConstants.IETF_PEN_VENDORID;

    /**
     * Private constructor should never be invoked.
     */
    private PbMessageFactoryIetf() {
        throw new AssertionError();
    }
    /**
     * Creates a TNCCS message containing an access recommendation.
     * @param recommendation the access recommendation
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createAccessRecommendation(
            final PbMessageAccessRecommendationEnum recommendation)
            throws ValidationException {

        byte flags = 0;
        long type = PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION
                .id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf
                        .createAccessRecommendationValue(recommendation));

    }

    /**
     * Creates a TNCCS message containing a TNCS assessment result.
     * @param result the assessment result
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createAssessmentResult(
            final PbMessageAssessmentResultEnum result)
            throws ValidationException {

        byte flags = PbMessageFlagEnum.NOSKIP.bit();
        long type = PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf.createAssessmentResultValue(result));

    }

    /**
     * Creates a TNCCS message with a experimental value.
     * @param content the message content
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createExperimental(final String content)
            throws ValidationException {

        byte flags = 0;
        long type = PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf.createExperimentalValue(content));

    }

    /**
     * Creates a TNCCS message containing an integrity measurement component
     * message. Uses the given values to address the message.
     * @param imFlags the flags to set for the message
     * @param subVendorId the vendor ID of the component
     * @param subType the type of the component
     * @param collectorId the source/destination IM(C) ID
     * @param validatorId the source/destination IM(V) ID
     * @param imMessage the serialized integrity measurement component message
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createIm(final PbMessageImFlagEnum[] imFlags,
            final long subVendorId, final long subType, final int collectorId,
            final int validatorId, final byte[] imMessage)
            throws ValidationException {

        byte flags = PbMessageFlagEnum.NOSKIP.bit();
        long type = PbMessageTypeEnum.IETF_PB_PA.id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf.createImValue(imFlags, subVendorId,
                        subType, collectorId, validatorId, imMessage));

    }

    /**
     * Creates a TNCCS message suggesting a preferred language for human
     * readable strings.
     * @param preferredLanguage the RFC 3282 preferred language
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createLanguagePreference(
            final String preferredLanguage) throws ValidationException {

        byte flags = 0;
        long type = PbMessageTypeEnum.IETF_PB_LANGUAGE_PREFERENCE.id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf
                        .createLanguagePreferenceValue(preferredLanguage));

    }

    /**
     * Creates a TNCCS message with a string describing a reason for an
     * assessment result and access recommendation in the given language.
     * @param reasonString the reason string
     * @param langCode the RFC 4646 language code
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createReasonString(final String reasonString,
            final String langCode) throws ValidationException {

        byte flags = 0;
        long type = PbMessageTypeEnum.IETF_PB_REASON_STRING.id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf.createReasonStringValue(reasonString,
                        langCode));

    }

    /**
     * Creates a TNCCS message containing a simple error.
     * @param errorFlags the flags to set for the error
     * @param errorCode the code describing the error
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createErrorSimple(
            final PbMessageErrorFlagsEnum[] errorFlags,
            final PbMessageErrorCodeEnum errorCode) throws ValidationException {

        byte flags = PbMessageFlagEnum.NOSKIP.bit();
        long type = PbMessageTypeEnum.IETF_PB_ERROR.id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf.createErrorValueSimple(
                        errorFlags, errorCode.code()));
    }

    /**
     * Creates a TNCCS message containing an error with information where
     * the error occurred in another message.
     * @param errorFlags the flags to set for the error
     * @param errorCode the code describing the error
     * @param offset the offset from the message beginning to the
     * erroneous value
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createErrorOffset(
            final PbMessageErrorFlagsEnum[] errorFlags,
            final PbMessageErrorCodeEnum errorCode, final long offset)
            throws ValidationException {

        byte flags = PbMessageFlagEnum.NOSKIP.bit();
        long type = PbMessageTypeEnum.IETF_PB_ERROR.id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf.createErrorValueWithOffset(
                        errorFlags, errorCode.code(), offset));
    }

    /**
     * Creates a TNCCS message containing an error with information about the
     * supported version.
     * @param errorFlags the flags to set for the error
     * @param badVersion the unsupported version
     * @param maxVersion the maximum supported version
     * @param minVersion the minimum supported version
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createErrorVersion(
            final PbMessageErrorFlagsEnum[] errorFlags,
            final short badVersion,
            final short maxVersion, final short minVersion)
                    throws ValidationException {

        byte flags = PbMessageFlagEnum.NOSKIP.bit();
        long type = PbMessageTypeEnum.IETF_PB_ERROR.id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf.createErrorValueWithVersion(
                        errorFlags, badVersion, maxVersion, minVersion));
    }

    /**
     * Creates a TNCCS message with a remediation string in a given language.
     * @param remediationString the remediation string
     * @param langCode the RFC 4646 language code
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createRemediationParameterString(
            final String remediationString, final String langCode)
            throws ValidationException {

        byte flags = 0;
        long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS
                .id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf.createRemediationParameterString(
                        remediationString, langCode));

    }

    /**
     * Creates a TNCCS message with a remediation URI for further information/
     * actions.
     * @param uri the remediation URI
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbMessage createRemediationParameterUri(final String uri)
            throws ValidationException {

        byte flags = 0;
        long type = PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS
                .id();

        return createMessage(flags, type,
                PbMessageValueFactoryIetf.createRemediationParameterUri(uri));

    }

    /**
     * Creates a message with the given values.
     * @param flags the flags to set for the message
     * @param type the message type
     * @param value the message value
     * @return an IETF RFC 5793 compliant TNCCS message
     * @throws ValidationException if creation fails because of invalid values
     */
    private static PbMessage createMessage(final byte flags, final long type,
            final AbstractPbMessageValue value) throws ValidationException {
        NotNull.check("Value cannot be null.", value);

        PbMessageHeaderBuilderIetf mBuilder = new PbMessageHeaderBuilderIetf();
        try {
            mBuilder.setFlags(flags);
            mBuilder.setVendorId(VENDORID);
            mBuilder.setType(type);
            mBuilder.setLength(PbMessageTlvFixedLengthEnum.MESSAGE.length()
                    + value.getLength());
        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e,
                    ValidationException.OFFSET_NOT_SET);
        }

        PbMessage message = new PbMessage(mBuilder.toObject(), value);

        return message;

    }
}
