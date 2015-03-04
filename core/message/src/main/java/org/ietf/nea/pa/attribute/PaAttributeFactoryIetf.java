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
package org.ietf.nea.pa.attribute;

import java.util.Date;
import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeAssessmentResultEnum;
import org.ietf.nea.pa.attribute.enums
.PaAttributeFactoryDefaultPasswordStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeForwardingStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationLastResultEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeOperationStatusEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.attribute.util.PackageEntry;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Factory utility to create an IETF RFC 5792 compliant integrity measurement
 * attribute.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class PaAttributeFactoryIetf {

    private static final long VENDORID = IETFConstants.IETF_PEN_VENDORID;

    /**
     * Private constructor should never be invoked.
     */
    private PaAttributeFactoryIetf() {
        throw new AssertionError();
    }

    /**
     * Creates an attribute with a test value.
     * @param content the attribute content
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createTestValue(final String content)
            throws ValidationException {

        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_TESTING.id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf.createTestValue(content));

    }

    /**
     * Creates an attribute with an assessment result.
     * @param result the assessment result
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createAssessmentResult(
            final PaAttributeAssessmentResultEnum result)
            throws ValidationException {

        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_ASSESSMENT_RESULT
                .id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf
                .createAssessmentResultValue(result));

    }

    /**
     * Creates an attribute that indicates if a default password is used.
     * @param status indicator indicating if default password is used
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createFactoryDefaultPassword(
            final PaAttributeFactoryDefaultPasswordStatusEnum status)
            throws ValidationException {

        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_FACTORY_DEFAULT_PW_ENABLED
                .id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf
                        .createFactoryDefaultPasswordValue(status));

    }

    /**
     * Creates an attribute that indicates if traffic forwarding is enabled.
     * @param status indicator indicating if traffic forwarding is enabled
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createForwardingEnabled(
            final PaAttributeForwardingStatusEnum status)
            throws ValidationException {

        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_FORWARDING_ENABLED
                .id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf
                        .createForwardingEnabledValue(status));

    }

    /**
     * Creates an attribute containing product information.
     * @param vendorId the vendor ID of the product vendor
     * @param productId the product ID given by the vendor
     * @param productName the product name
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createProductInformation(final long vendorId,
            final int productId, final String productName)
                    throws ValidationException {
        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION
                .id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf.createProductInformationValue(
                        vendorId, productId, productName));
    }

    /**
     * Creates an attribute with string based version information.
     * @param versionNumber the version number
     * @param buildVersion the build version number
     * @param configVersion the configuration version number
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createStringVersion(final String versionNumber,
            final String buildVersion, final String configVersion)
            throws ValidationException {
        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_STRING_VERSION.id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf.createStringVersionValue(
                        versionNumber, buildVersion, configVersion));
    }

    /**
     * Creates an attribute with numeric version information.
     * @param majorVersion the major version number
     * @param minorVersion the minor version number
     * @param buildVersion the build version number
     * @param servicePackMajor the major service pack number
     * @param servicePackMinor the minor service pack number
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createNumericVersion(final long majorVersion,
            final long minorVersion, final long buildVersion,
            final int servicePackMajor, final int servicePackMinor)
            throws ValidationException {

        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION.id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf.createNumericVersionValue(
                        majorVersion, minorVersion, buildVersion,
                        servicePackMajor, servicePackMinor));

    }

    /**
     * Creates an attribute containing information about installed packages.
     * @param packages a list of installed packages
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createInstalledPackages(
            final List<PackageEntry> packages) throws ValidationException {
        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_INSTALLED_PACKAGES
                .id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf
                        .createInstalledPackagesValue(packages));
    }

    /**
     * Creates an attribute describing the operational status.
     * @param status the operational status
     * @param result the result of the last execution
     * @param lastUse the last execution
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createOperationalStatus(
            final PaAttributeOperationStatusEnum status,
            final PaAttributeOperationLastResultEnum result, final Date lastUse)
            throws ValidationException {
        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_OPERATIONAL_STATUS
                .id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf.createOperationalStatusValue(
                        status, result, lastUse));
    }

    /**
     * Creates an attribute containing information about active port filter
     * rules. Must contain at least one rule.
     * @param first the mandatory rule
     * @param more further rules
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createPortFiler(final PortFilterEntry first,
            final PortFilterEntry... more) throws ValidationException {
        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_PORT_FILTER.id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf.createPortFilterValue(first, more));
    }

    /**
     * Creates an attribute requesting attributes. Must contain at least one
     * attribute request.
     * @param first the mandatory attribute request
     * @param more further attribute requests
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createAttributeRequest(
            final AttributeReferenceEntry first,
            final AttributeReferenceEntry... more) throws ValidationException {
        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST
                .id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf.createAttributeRequestValue(first,
                        more));
    }

    /**
     * Creates an attribute with a remediation string in the given language.
     * @param remediationString the remediation string
     * @param langCode the RFC 4646 language code
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createRemediationParameterString(
            final String remediationString, final String langCode)
            throws ValidationException {


        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_REMEDIATION_INSTRUCTIONS
                .id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf.createRemediationParameterString(
                        remediationString, langCode));

    }

    /**
     * Creates an attribute with a remediation URI for further information/
     * actions.
     * @param uri the remediation URI
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createRemediationParameterUri(final String uri)
            throws ValidationException {

        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_REMEDIATION_INSTRUCTIONS
                .id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf.createRemediationParameterUri(uri));

    }

    /**
     * Creates an error attribute containing information about an invalid
     * parameter.
     * @param messageHeaderCopy the byte copy of the message header of
     * the erroneous message
     * @param errorOffset the offset from the message beginning to the
     * erroneous value
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createErrorInformationInvalidParam(
            final byte[] messageHeaderCopy, final long errorOffset)
            throws ValidationException {

        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_ERROR.id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf
                        .createErrorInformationInvalidParameter(
                                messageHeaderCopy, errorOffset));

    }

    /**
     * Creates an error attribute containing information about the supported
     * version.
     * @param messageHeaderCopy the byte copy of the message header of
     * the erroneous message
     * @param maxVersion the maximum version supported
     * @param minVersion the minimum version supported
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createErrorInformationUnsupportedVersion(
            final byte[] messageHeaderCopy, final short maxVersion,
            final short minVersion) throws ValidationException {


        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_ERROR.id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf
                        .createErrorInformationUnsupportedVersion(
                                messageHeaderCopy,
                                maxVersion, minVersion));

    }

    /**
     * Creates an error attribute containing information about an unsupported
     * but mandatory attribute.
     * @param messageHeaderCopy the byte copy of the message header of
     * the erroneous message
     * @param attributeHeaderCopy the copy of the header of the unsupported
     * attribute
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PaAttribute createErrorInformationUnsupportedAttribute(
            final byte[] messageHeaderCopy,
            final PaAttributeHeader attributeHeaderCopy)
            throws ValidationException {

        byte flags = 0;
        long type = PaAttributeTypeEnum.IETF_PA_ERROR.id();

        return createAttribute(flags, type,
                PaAttributeValueFactoryIetf
                        .createErrorInformationUnsupportedAttribute(
                                messageHeaderCopy,
                                attributeHeaderCopy));

    }

    /**
     * Creates an attribute with the given values.
     * @param flags the flags to set for the attribute
     * @param type the attribute type
     * @param value the attribute value
     * @return an IETF RFC 5792 compliant integrity measurement attribute
     * @throws ValidationException if creation fails because of invalid values
     */
    private static PaAttribute createAttribute(final byte flags,
            final long type, final AbstractPaAttributeValue value)
            throws ValidationException {
        NotNull.check("Value cannot be null.", value);

        PaAttributeHeaderBuilderIetf aBuilder =
                new PaAttributeHeaderBuilderIetf();
        try {
            aBuilder.setFlags(flags);
            aBuilder.setVendorId(VENDORID);
            aBuilder.setType(type);
            aBuilder.setLength(PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length()
                    + value.getLength());
        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e,
                    ValidationException.OFFSET_NOT_SET);
        }
        PaAttribute attribute = new PaAttribute(aBuilder.toObject(), value);

        return attribute;

    }
}
