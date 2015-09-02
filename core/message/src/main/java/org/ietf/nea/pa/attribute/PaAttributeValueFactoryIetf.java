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
package org.ietf.nea.pa.attribute;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums
.PaAttributeFactoryDefaultPasswordStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeForwardingStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationLastResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributePortFilterStatus;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueErrorInformation;
import org.ietf.nea.pa.attribute.util
.AbstractPaAttributeValueRemediationParameter;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationFactoryIetf;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationInvalidParam;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedAttribute;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationUnsupportedVersion;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueRemediationParameterFactoryIetf;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueRemediationParameterString;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueRemediationParameterUri;
import org.ietf.nea.pa.attribute.util.PackageEntry;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Factory utility to create an IETF RFC 5792 compliant
 * integrity measurement attribute value.
 *
 *
 */
public final class PaAttributeValueFactoryIetf {

    private static final long VENDORID = IETFConstants.IETF_PEN_VENDORID;

    /**
     * Private constructor should never be invoked.
     */
    private PaAttributeValueFactoryIetf() {
        throw new AssertionError();
    }

    /**
     * Creates a test attribute value with the given content.
     * @param content the value content
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static AbstractPaAttributeValue createTestValue(
            final String content) {
        String c = (content != null) ? content : "";

        return new PaAttributeValueTesting(c.getBytes(Charset
                .forName("UTF-8")).length, c);
    }

    /**
     * Creates an attribute value with an assessment result.
     * @param result the assessment result
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueAssessmentResult createAssessmentResultValue(
            final PaAttributeAssessmentResultEnum result) {

        NotNull.check("Result cannot be null.", result);

        return new PaAttributeValueAssessmentResult(
                PaAttributeTlvFixedLengthEnum.ASS_RES.length(), result);
    }

    /**
     * Creates an attribute value, that indicates if a default password is
     * used.
     * @param status indicator indicating if default password is used
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueFactoryDefaultPasswordEnabled
        createFactoryDefaultPasswordValue(
            final PaAttributeFactoryDefaultPasswordStatusEnum status) {

        NotNull.check("Status cannot be null.", status);

        return new PaAttributeValueFactoryDefaultPasswordEnabled(
                PaAttributeTlvFixedLengthEnum.FAC_PW.length(), status);
    }

    /**
     * Creates an attribute value, that indicates if traffic forwarding is
     * enabled.
     * @param status indicator indicating if traffic forwarding is enabled
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueForwardingEnabled
        createForwardingEnabledValue(
            final PaAttributeForwardingStatusEnum status) {

        NotNull.check("Status cannot be null.", status);

        return new PaAttributeValueForwardingEnabled(
                PaAttributeTlvFixedLengthEnum.FWD_EN.length(), status);
    }

    /**
     * Creates an attribute request value containing references to attributes.
     * Must contain at least one attribute reference.
     * @param reference the mandatory attribute request
     * @param moreReferences further attribute requests
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueAttributeRequest createAttributeRequestValue(
            final AttributeReferenceEntry reference,
            final AttributeReferenceEntry... moreReferences) {

        NotNull.check(
                "Reference cannot be null, "
                + "there must be at least one reference.",
                reference);

        List<AttributeReferenceEntry> referenceList = new LinkedList<>();
        referenceList.add(reference);
        if (moreReferences != null) {
            for (AttributeReferenceEntry attributeReference : moreReferences) {
                if (attributeReference != null) {
                    referenceList.add(attributeReference);
                }
            }
        }

        long length = PaAttributeTlvFixedLengthEnum.ATT_REQ.length()
                * referenceList.size();

        return new PaAttributeValueAttributeRequest(length, referenceList);
    }

    /**
     * Creates an attribute value containing product information.
     * @param vendorId the vendor ID of the product vendor
     * @param productId the product ID given by the vendor
     * @param productName the product name
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueProductInformation
        createProductInformationValue(final long vendorId, final int productId,
                final String productName) {

        if (vendorId == 0 && productId != 0) {
            throw new IllegalArgumentException(
                    "Because vendor ID is zero product ID must be zero.");
        }

        String pName = (productName != null) ? productName : "";

        long length = PaAttributeTlvFixedLengthEnum.PRO_INF.length();
        if (pName.length() > 0) {
            length += pName.getBytes(Charset.forName("UTF-8")).length;
        }

        return new PaAttributeValueProductInformation(length, vendorId,
                productId, pName);
    }

    /**
     * Creates an attribute value with numeric version information.
     * @param majorVersion the major version number
     * @param minorVersion the minor version number
     * @param buildVersion the build version number
     * @param servicePackMajor the major service pack number
     * @param servicePackMinor the minor service pack number
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueNumericVersion createNumericVersionValue(
            final long majorVersion, final long minorVersion,
            final long buildVersion,
            final int servicePackMajor, final int servicePackMinor) {
        /*
         * Nothing to check here. Maybe negative values are bad, but could not
         * find something, that mentioned this.
         */
        return new PaAttributeValueNumericVersion(
                PaAttributeTlvFixedLengthEnum.NUM_VER.length(), majorVersion,
                minorVersion, buildVersion, servicePackMajor, servicePackMinor);
    }

    /**
     * Creates an attribute value with string based version information.
     * @param versionNumber the version number
     * @param buildVersion the build version number
     * @param configVersion the configuration version number
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueStringVersion createStringVersionValue(
            final String versionNumber, final String buildVersion,
            final String configVersion) {
        String version = (versionNumber != null) ? versionNumber : "";
        String build = (buildVersion != null) ? buildVersion : "";
        String config = (configVersion != null) ? configVersion : "";


        if (version.length() > 0xFF) {
            throw new IllegalArgumentException("Version number length "
                    + version.length() + "is to long.");
        }

        if (build.length() > 0xFF) {
            throw new IllegalArgumentException("Build number length "
                    + build.length() + "is to long.");
        }

        if (config.length() > 0xFF) {
            throw new IllegalArgumentException("Build number length "
                    + config.length() + "is to long.");
        }

        long length = PaAttributeTlvFixedLengthEnum.STR_VER.length();
        if (version.length() > 0) {
            length += version.getBytes(Charset.forName("UTF-8")).length;
        }
        if (build.length() > 0) {
            length += build.getBytes(Charset.forName("UTF-8")).length;
        }
        if (config.length() > 0) {
            length += config.getBytes(Charset.forName("UTF-8")).length;
        }

        return new PaAttributeValueStringVersion(length, version,
                build, config);
    }

    /**
     * Creates an attribute value describing the operational status.
     * @param status the operational status
     * @param result the result of the last execution
     * @param lastUse the last execution
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueOperationalStatus
        createOperationalStatusValue(
            final PaAttributeOperationStatusEnum status,
            final PaAttributeOperationLastResultEnum result,
            final Date lastUse) {

        NotNull.check("Operational status cannot be null.", status);
        NotNull.check("Operational result cannot be null.", result);

        Date lUse = lastUse;
        if (lUse == null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(TimeZone.getTimeZone("Zulu"));
            cal.set(0, 0, 0, 0, 0, 0);
            lUse = cal.getTime();
        }

        return new PaAttributeValueOperationalStatus(
                PaAttributeTlvFixedLengthEnum.OP_STAT.length(), status, result,
                lUse);
    }

    /**
     * Creates an attribute value containing entries of active port filter
     * rules. Must contain at least one entry.
     * @param entry the mandatory rule
     * @param moreEntries further rules
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValuePortFilter createPortFilterValue(
            final PortFilterEntry entry, final PortFilterEntry... moreEntries) {

        NotNull.check(
                "Entry cannot be null, there must be at least one entry.",
                entry);

        List<PortFilterEntry> entries = new LinkedList<>();
        entries.add(entry);

        // check duplicates
        if (moreEntries != null) {
            Map<Short, Map<Integer, PaAttributePortFilterStatus>> sorted =
                    new HashMap<>();
            sorted.put(entry.getProtocolNumber(),
                    new HashMap<Integer, PaAttributePortFilterStatus>());
            sorted.get(entry.getProtocolNumber()).put(entry.getPortNumber(),
                    entry.getFilterStatus());

            for (PortFilterEntry pe : moreEntries) {
                if (pe != null) {
                    if (sorted.containsKey(pe.getProtocolNumber())) {
                        if (sorted.get(pe.getProtocolNumber()).containsKey(
                                pe.getPortNumber())) {
                            if (sorted.get(pe.getProtocolNumber()).values()
                                    .iterator().next()
                                    != pe.getFilterStatus()) {
                                throw new IllegalArgumentException(
                                        "The port filter contains entries"
                                        + " for the protocol "
                                        + pe.getProtocolNumber()
                                        + " with differnt blocking status.");
                            }
                            if (sorted.get(pe.getProtocolNumber()).get(
                                    pe.getPortNumber())
                                    != pe.getFilterStatus()) {
                                throw new IllegalArgumentException(
                                        "The port filter contains "
                                        + "duplicate entries for the tupel "
                                        + pe.getProtocolNumber()
                                        + ":"
                                        + pe.getPortNumber()
                                        + " with differnt blocking status.");
                            } else {
                                throw new IllegalArgumentException(
                                        "The port filter contains "
                                        + "duplicate entries for the tupel "
                                                + pe.getProtocolNumber() + ":"
                                                + pe.getPortNumber() + ".");
                            }
                        } else {
                            sorted.get(pe.getProtocolNumber()).put(
                                    pe.getPortNumber(), pe.getFilterStatus());
                        }
                    } else {
                        sorted.put(pe.getProtocolNumber(),
                                new HashMap<Integer,
                                PaAttributePortFilterStatus>());
                        sorted.get(pe.getProtocolNumber()).put(
                                pe.getPortNumber(), pe.getFilterStatus());
                    }
                }
            }

            entries.addAll(Arrays.asList(moreEntries));
        }

        long length = PaAttributeTlvFixedLengthEnum.PORT_FT.length()
                * entries.size();

        return new PaAttributeValuePortFilter(length, entries);
    }

    /**
     * Creates an attribute value containing information about installed
     * packages.
     * @param packages a list of installed packages
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueInstalledPackages
        createInstalledPackagesValue(
            final List<PackageEntry> packages) {

        List<PackageEntry> entries = (packages != null) ? packages
                : new ArrayList<PackageEntry>();

        long length = PaAttributeTlvFixedLengthEnum.INS_PKG.length();

        for (PackageEntry pkgEntry : entries) {
            length += pkgEntry.getPackageNameLength();
            length += pkgEntry.getPackageVersionLength();
            length += 2; // +2 for the fields, that contain the length
        }

        return new PaAttributeValueInstalledPackages(length, entries);
    }

    /**
     * Creates an attribute value with a remediation string in the given
     * language.
     * @param remediationString the remediation string
     * @param langCode the RFC 4646 language code
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueRemediationParameters
        createRemediationParameterString(
            final String remediationString, final String langCode) {

        long rpVendorId = VENDORID;
        long rpType = PbMessageRemediationParameterTypeEnum.IETF_URI.id();

        PaAttributeValueRemediationParameterString parameter =
                PaAttributeValueRemediationParameterFactoryIetf
                .createRemediationParameterString(remediationString, langCode);

        return createRemediationParametersValue(rpVendorId, rpType, parameter);
    }

    /**
     * Creates an attribute value with a remediation URI for further
     * information/actions.
     * @param uri the remediation URI
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueRemediationParameters
        createRemediationParameterUri(final String uri) {

        long rpVendorId = VENDORID;
        long rpType = PbMessageRemediationParameterTypeEnum.IETF_URI.id();

        PaAttributeValueRemediationParameterUri parameter =
                PaAttributeValueRemediationParameterFactoryIetf
                .createRemediationParameterUri(uri);

        return createRemediationParametersValue(rpVendorId, rpType, parameter);

    }

    /**
     * Creates an attribute value with a remediation parameter.
     * @param rpVendorId the vendor ID of the remediation string
     * @param rpType the type of the remediation string
     * @param parameter the remediation parameter
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    private static PaAttributeValueRemediationParameters
        createRemediationParametersValue(final long rpVendorId,
                final long rpType,
                final AbstractPaAttributeValueRemediationParameter parameter) {

        if (rpVendorId > IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new IllegalArgumentException("Vendor ID is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
        }
        if (rpType > IETFConstants.IETF_MAX_TYPE) {
            throw new IllegalArgumentException("Type is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
        }

        long length = PaAttributeTlvFixedLengthEnum.REM_PAR.length()
                + parameter.getLength();

        return new PaAttributeValueRemediationParameters(rpVendorId, rpType,
                length, parameter);
    }

    /**
     * Creates an error attribute value containing information about the
     * supported version.
     * @param messageHeader the byte copy of the message header of
     * the erroneous message
     * @param maxVersion the maximum version supported
     * @param minVersion the minimum version supported
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueError
        createErrorInformationUnsupportedVersion(final byte[] messageHeader,
                final short maxVersion, final short minVersion) {

        long errVendorId = VENDORID;
        long errCode = PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_VERSION
                .code();

        PaAttributeValueErrorInformationUnsupportedVersion errorInformation =
                PaAttributeValueErrorInformationFactoryIetf
                .createErrorInformationUnsupportedVersion(messageHeader,
                        maxVersion, minVersion);

        return createErrorValue(errVendorId, errCode, errorInformation);
    }

    /**
     * Creates an error attribute value containing information about an
     * unsupported but mandatory attribute.
     * @param messageHeader the byte copy of the message header of
     * the erroneous message
     * @param attributeHeader the copy of the header of the unsupported
     * attribute
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueError
        createErrorInformationUnsupportedAttribute(
            final byte[] messageHeader,
            final PaAttributeHeader attributeHeader) {

        long errVendorId = VENDORID;
        long errCode = PaAttributeErrorCodeEnum
                .IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code();

        PaAttributeValueErrorInformationUnsupportedAttribute errorInformation =
                PaAttributeValueErrorInformationFactoryIetf
                .createErrorInformationUnsupportedAttribute(
                        messageHeader, attributeHeader);

        return createErrorValue(errVendorId, errCode, errorInformation);
    }

    /**
     * Creates an error attribute value containing information about an invalid
     * parameter.
     * @param messageHeader the byte copy of the message header of
     * the erroneous message
     * @param offset the offset from the message beginning to the
     * erroneous value
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    public static PaAttributeValueError createErrorInformationInvalidParameter(
            final byte[] messageHeader, final long offset) {

        long errVendorId = VENDORID;
        long errCode = PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code();

        PaAttributeValueErrorInformationInvalidParam errorInformation =
                PaAttributeValueErrorInformationFactoryIetf
                .createErrorInformationInvalidParameter(messageHeader, offset);

        return createErrorValue(errVendorId, errCode, errorInformation);
    }

    /**
     * Create an error attribute value with an error parameter.
     * @param errVendorId the vendor ID of the error
     * @param errCode the code describing the error
     * @param errorInformation the error parameter
     * @return an IETF RFC 5792 compliant integrity measurement attribute value
     */
    private static PaAttributeValueError createErrorValue(
            final long errVendorId, final long errCode,
            final AbstractPaAttributeValueErrorInformation errorInformation) {

        if (errVendorId > IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new IllegalArgumentException("Vendor ID is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_VENDOR_ID) + ".");
        }
        if (errCode > IETFConstants.IETF_MAX_TYPE) {
            throw new IllegalArgumentException("Code is greater than "
                    + Long.toString(IETFConstants.IETF_MAX_TYPE) + ".");
        }

        long length = PaAttributeTlvFixedLengthEnum.ERR_INF.length()
                + errorInformation.getLength();

        return new PaAttributeValueError(length, errVendorId, errCode,
                errorInformation);
    }

}
