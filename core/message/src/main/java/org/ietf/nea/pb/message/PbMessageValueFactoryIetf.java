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

import java.nio.charset.Charset;

import org.ietf.nea.pb.message.enums.PbMessageAccessRecommendationEnum;
import org.ietf.nea.pb.message.enums.PbMessageAssessmentResultEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagEnum;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueErrorParameter;
import org.ietf.nea.pb.message.util.AbstractPbMessageValueRemediationParameter;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterFactoryIetf;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;
import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;
import org.ietf.nea.pb.message.util
.PbMessageValueRemediationParameterFactoryIetf;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Factory utility to create a IETF RFC 5793 compliant TNCCS message value.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class PbMessageValueFactoryIetf {

    private static final long VENDORID = IETFConstants.IETF_PEN_VENDORID;

    /**
     * Private constructor should never be invoked.
     */
    private PbMessageValueFactoryIetf() {
        throw new AssertionError();
    }

    /**
     * Creates a TNCCS message value containing an integrity measurement
     * component message. Uses the given values to address the message.
     * @param imFlags the flags to set for the message
     * @param subVendorId the vendor ID of the component
     * @param subType the type of the component
     * @param collectorId the source/destination IM(C) ID
     * @param validatorId the source/destination IM(V) ID
     * @param message the serialized integrity measurement component message
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueIm createImValue(
            final PbMessageImFlagEnum[] imFlags, final long subVendorId,
            final long subType, final int collectorId, final int validatorId,
            final byte[] message) {

        NotNull.check("Error flags cannot be null.", (Object) imFlags);
        byte[] imMessage = (message != null) ? message : new byte[0];

        if (subVendorId > IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new IllegalArgumentException("Vendor ID is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
        }
        if (subType > IETFConstants.IETF_MAX_TYPE) {
            throw new IllegalArgumentException("Type is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
        }

        long length = PbMessageTlvFixedLengthEnum.IM_VALUE.length()
                + imMessage.length;

        return new PbMessageValueIm(imFlags, subVendorId, subType, collectorId,
                validatorId, length, imMessage);
    }

    /**
     * Creates a TNCCS message value containing an access recommendation.
     * @param recommendation the access recommendation
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueAccessRecommendation
        createAccessRecommendationValue(
            final PbMessageAccessRecommendationEnum recommendation) {

        NotNull.check("Recommendation cannot be null.", recommendation);

        return new PbMessageValueAccessRecommendation(
                PbMessageTlvFixedLengthEnum.ACC_REC_VALUE.length(),
                recommendation);
    }

    /**
     * Creates a TNCCS message value containing a TNCS assessment result.
     * @param result the assessment result
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueAssessmentResult createAssessmentResultValue(
            final PbMessageAssessmentResultEnum result) {

        NotNull.check("Result cannot be null.", result);

        return new PbMessageValueAssessmentResult(
                PbMessageTlvFixedLengthEnum.ASS_RES_VALUE.length(), result);
    }

    /**
     * Creates a TNCCS message error value containing information where
     * the error occurred in another message.
     * @param errorFlags the flags to set for the error
     * @param errorCode the code describing the error
     * @param offset the offset from the message beginning to the
     * erroneous value
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueError createErrorValueWithOffset(
            final PbMessageErrorFlagsEnum[] errorFlags,
            final int errorCode, final long offset) {
        NotNull.check("Error flags cannot be null.", (Object) errorFlags);

        PbMessageValueErrorParameterOffset errorParameter =
                PbMessageValueErrorParameterFactoryIetf
                .createErrorParameterOffset(VENDORID, errorCode, offset);

        return createError(errorFlags, VENDORID, errorCode,
                errorParameter);
    }

    /**
     * Creates a TNCCS message error value containing information about the
     * supported version.
     * @param errorFlags the flags to set for the error
     * @param badVersion the unsupported version
     * @param maxVersion the maximum supported version
     * @param minVersion the minimum supported version
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueError createErrorValueWithVersion(
            final PbMessageErrorFlagsEnum[] errorFlags,
            final short badVersion, final short maxVersion,
            final short minVersion) {
        NotNull.check("Error flags cannot be null.", (Object) errorFlags);

        PbMessageValueErrorParameterVersion errorParameter =
                PbMessageValueErrorParameterFactoryIetf
                    .createErrorParameterVersion(
                        badVersion, maxVersion, minVersion);

        return createError(errorFlags, VENDORID,
                PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code(),
                errorParameter);

    }

    /**
     * Creates a TNCCS message error value containing a simple error.
     * @param errorFlags the flags to set for the error
     * @param errorCode the code describing the error
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueError createErrorValueSimple(
            final PbMessageErrorFlagsEnum[] errorFlags,
            final int errorCode) {
        NotNull.check("Error flags cannot be null.", (Object) errorFlags);

        if (errorCode == PbMessageErrorCodeEnum.IETF_LOCAL.code()
                    || errorCode == PbMessageErrorCodeEnum
                        .IETF_UNEXPECTED_BATCH_TYPE.code()) {

            return createError(errorFlags, VENDORID, errorCode, null);
        }

        throw new IllegalArgumentException(
                "Error parameter cannot be null for error with vendor ID "
                        + VENDORID + " and with code " + errorCode + ".");
    }

    /**
     * Creates a TNCCS message value suggesting a preferred language for human
     * readable strings.
     * @param preferredLanguage the RFC 3282 preferred language
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueLanguagePreference
        createLanguagePreferenceValue(
            final String preferredLanguage) {

        String lang = (preferredLanguage != null) ? preferredLanguage : "";

        long length =
                lang.getBytes(Charset.forName("US-ASCII")).length;

        return new PbMessageValueLanguagePreference(length, lang);

    }

    /**
     * Creates a TNCCS message value with an experimental value.
     * @param content the value content
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueExperimental createExperimentalValue(
            final String content) {

        String m = (content != null) ? content : "";

        return new PbMessageValueExperimental(m.length(), m);
    }

    /**
     * Creates a TNCCS message value with a string describing a reason for an
     * assessment result and access recommendation in the given language.
     * @param reasonString the reason string
     * @param langCode the RFC 4646 language code
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueReasonString createReasonStringValue(
            final String reasonString, final String langCode) {

        String reason = (reasonString != null) ? reasonString : "";
        String lang = (langCode != null) ? langCode : "";

        if (lang.length() > 0xFF) {
            throw new IllegalArgumentException("Language code length "
                    + lang.length() + "is to long.");
        }

        long length = PbMessageTlvFixedLengthEnum.REA_STR_VALUE.length();
        if (reason.length() > 0) {
            length += reason.getBytes(Charset.forName("UTF-8")).length;
        }
        if (lang.length() > 0) {
            length += lang.getBytes(Charset.forName("US-ASCII")).length;
        }

        return new PbMessageValueReasonString(length, reason, lang);
    }

    /**
     * Creates a TNCCS message value with a remediation string in a given
     * language.
     * @param remediationString the remediation string
     * @param langCode the RFC 4646 language code
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueRemediationParameters
        createRemediationParameterString(final String remediationString,
                final String langCode) {

        long rpVendorId = VENDORID;
        long rpType = PbMessageRemediationParameterTypeEnum.IETF_STRING.id();

        PbMessageValueRemediationParameterString parameter =
                PbMessageValueRemediationParameterFactoryIetf
                .createRemediationParameterString(remediationString, langCode);

        return createRemediationParameter(rpVendorId, rpType, parameter);
    }

    /**
     * Creates a TNCCS message value with a remediation URI for further
     * information/actions.
     * @param uri the remediation URI
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    public static PbMessageValueRemediationParameters
        createRemediationParameterUri(final String uri) {

        long rpVendorId = VENDORID;
        long rpType = PbMessageRemediationParameterTypeEnum.IETF_URI.id();

        PbMessageValueRemediationParameterUri parameter =
                PbMessageValueRemediationParameterFactoryIetf
                .createRemediationParameterUri(uri);

        return createRemediationParameter(rpVendorId, rpType, parameter);

    }

    /**
     * Creates a TNCCS message value with a remediation parameter.
     * @param rpVendorId the vendor ID of the remediation string
     * @param rpType the type of the remediation string
     * @param parameter the remediation parameter
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    private static PbMessageValueRemediationParameters
        createRemediationParameter(final long rpVendorId, final long rpType,
            final AbstractPbMessageValueRemediationParameter parameter) {

        if (rpVendorId > IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new IllegalArgumentException("Vendor ID is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
        }
        if (rpType > IETFConstants.IETF_MAX_TYPE) {
            throw new IllegalArgumentException("Type is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
        }

        long length = PbMessageTlvFixedLengthEnum.REM_PAR_VALUE.length()
                + parameter.getLength();

        return new PbMessageValueRemediationParameters(rpVendorId, rpType,
                length, parameter);
    }

    /**
     * Create a TNCCS message error values with an error parameter.
     * @param flags the flags to set for the error
     * @param errorVendorId the vendor ID of the error
     * @param errorCode the code describing the error
     * @param errorParameter the error parameter
     * @return an IETF RFC 5793 compliant TNCCS message value
     */
    private static PbMessageValueError createError(
            final PbMessageErrorFlagsEnum[] flags, final long errorVendorId,
            final int errorCode,
            final AbstractPbMessageValueErrorParameter errorParameter) {

        if (errorVendorId > IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new IllegalArgumentException("Vendor ID is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
        }
        if (errorCode > IETFConstants.IETF_MAX_ERROR_CODE) {
            throw new IllegalArgumentException("Code is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_ERROR_CODE) + ".");
        }

        long length = PbMessageTlvFixedLengthEnum.ERR_VALUE.length()
                + ((errorParameter != null) ? errorParameter.getLength() : 0);

        return new PbMessageValueError(flags, errorVendorId, errorCode, length,
                errorParameter);
    }

}
