package de.hsbremen.tc.tnc.im.evaluate.example.os;
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


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.attribute.util
.PaAttributeValueErrorInformationInvalidParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentTypeEnum;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImcEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.example.os.exception
.PatternNotFoundException;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;
import de.hsbremen.tc.tnc.natives.CLibrary;
import de.hsbremen.tc.tnc.natives.CLibrary.UTSNAME;

/**
 * Example operating system IMC evaluation unit, which uses
 * JNI to get system information.
 *
 *
 */
public class OsImcEvaluationUnit extends AbstractImcEvaluationUnitIetf {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(OsImcEvaluationUnit.class);

    public static final long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
    public static final long TYPE = PaComponentTypeEnum.IETF_PA_OPERATING_SYSTEM
            .id();

    /**
     * Create a the evaluation unit with the given handshake retry listener.
     *
     * @param globalHandshakeRetryListener the global handshake retry listener.
     */
    public OsImcEvaluationUnit(
            final GlobalHandshakeRetryListener globalHandshakeRetryListener) {
        super(globalHandshakeRetryListener);
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
        final UTSNAME systemDescription = new UTSNAME();
        CLibrary.INSTANCE.uname(systemDescription);

        List<ImAttribute> attributes = new ArrayList<>();
        try {
            PaAttribute prodInfo = this
                    .getProductInformation(systemDescription);
            attributes.add(prodInfo);
        } catch (ValidationException e) {
            LOGGER.error("Product information clould not be created.", e);
        }

        try {
            PaAttribute numericVers = this.getNumericVersion(systemDescription);
            attributes.add(numericVers);
        } catch (NumberFormatException | ValidationException
                | PatternNotFoundException e) {
            LOGGER.error("Numeric version clould not be created.", e);
        }

        try {
            PaAttribute stringVers = this.getStringVersion(systemDescription);
            attributes.add(stringVers);
        } catch (ValidationException e) {
            LOGGER.error("String version clould not be created.", e);
        }

        // PaAttribute forwardEnabled = this.getForwardingStatus();
        // attributes.add(forwardEnabled);
        //
        // PaAttribute facDefPwd = this.getFactoryDefaultPwdStatus();
        // attributes.add(facDefPwd);

        return attributes;
    }

    @Override
    public void terminate() {
        LOGGER.debug("Terminate called.");
    }

    @Override
    protected List<? extends ImAttribute> handleAttributeRequest(
            final PaAttributeValueAttributeRequest value,
            final ImSessionContext context) {

        List<PaAttribute> attributeList = new ArrayList<>();

        final UTSNAME systemDescription = new UTSNAME();
        CLibrary.INSTANCE.uname(systemDescription);

        List<AttributeReferenceEntry> references = value.getReferences();
        for (AttributeReferenceEntry attributeReference : references) {
            try {
                if (attributeReference.getVendorId() == 0) {
                    if (attributeReference.getType()
                            == PaAttributeTypeEnum.IETF_PA_PRODUCT_INFORMATION
                            .id()) {
                        attributeList.add(this
                                .getProductInformation(systemDescription));
                    } else if (attributeReference.getType()
                            == PaAttributeTypeEnum.IETF_PA_NUMERIC_VERSION
                            .id()) {
                        attributeList.add(this
                                .getNumericVersion(systemDescription));
                    } else if (attributeReference.getType()
                            == PaAttributeTypeEnum.IETF_PA_STRING_VERSION
                            .id()) {
                        attributeList.add(this
                                .getStringVersion(systemDescription));
                    }
                }
            } catch (ValidationException | NumberFormatException
                    | PatternNotFoundException e) {
                LOGGER.error("Requested attribute could not be created.", e);
            }
        }
        return attributeList;
    }

    @Override
    protected List<? extends ImAttribute> handleError(
            final PaAttributeValueError value, final ImSessionContext context) {
        // TODO implement error handling
        StringBuilder b = new StringBuilder();
        b.append("An error was received: \n")
                .append("Error with vendor ID ")
                .append(value.getErrorVendorId())
                .append(" and type ")
                .append(PaAttributeErrorCodeEnum.fromCode(value.getErrorCode()))
                .append(".\n")
                .append("Error was found in message ")
                .append(value.getErrorInformation().getMessageHeader()
                        .toString());

        if (value.getErrorInformation()
                instanceof PaAttributeValueErrorInformationInvalidParam) {
            b.append(" at offset ").append(
                    ((PaAttributeValueErrorInformationInvalidParam) value
                            .getErrorInformation()).getOffset());
        }
        b.append(".");
        LOGGER.error(b.toString());

        return new ArrayList<>(0);
    }

    @Override
    protected List<? extends ImAttribute> handleRemediation(
            final PaAttributeValueRemediationParameters value,
            final ImSessionContext context) {
        // TODO implement remediation handling.
        LOGGER.info("Remediation instructions were received.");

        // context.requestConnectionHandshakeRetry(
        //      ImHandshakeRetryReasonEnum
        //      .TNC_RETRY_REASON_IMC_REMEDIATION_COMPLETE);

        return new ArrayList<>(0);
    }

    @Override
    protected List<? extends ImAttribute> handleResult(
            final PaAttributeValueAssessmentResult value,
            final ImSessionContext context) {
        LOGGER.info("Assessment result is: " + value.getResult().toString()
                + " - (# " + value.getResult().id() + ")");
        return new ArrayList<>(0);
    }

    /**
     * Creates a message attribute containing the string representation
     * of the operating system version.
     *
     * @param systemDescription the system information object
     * @return the message attribute
     * @throws ValidationException if attribute creation fails
     */
    private PaAttribute getStringVersion(final UTSNAME systemDescription)
            throws ValidationException {
        return PaAttributeFactoryIetf.createStringVersion(new String(
                systemDescription.release).trim(), null, new String(
                systemDescription.machine).trim());
    }

    /**
     * Creates a message attribute containing the numeric representation
     * of the operating system version.
     *
     * @param systemDescription the system information object
     * @return the message attribute
     * @throws ValidationException if attribute creation fails
     * @throws PatternNotFoundException if the version number string has
     * not the expected format
     */
    private PaAttribute getNumericVersion(final UTSNAME systemDescription)
            throws ValidationException, PatternNotFoundException {
        String release = new String(systemDescription.release).trim();
        Pattern p = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");
        Matcher m = p.matcher(release);
        if (m.find()) {
            long majorVersion = Long.parseLong(m.group(1));
            long minorVersion = Long.parseLong(m.group(2));
            long buildVersion = Long.parseLong(m.group(3));
            int servicePackVersion = 0;
            int servicePackVersionMinor = 0;
            return PaAttributeFactoryIetf.createNumericVersion(majorVersion,
                    minorVersion, buildVersion, servicePackVersion,
                    servicePackVersionMinor);
        } else {
            throw new PatternNotFoundException("Version pattern "
                    + p.toString() + " was not found.", release, p.toString());
        }
    }

    /**
     * Creates a message attribute containing product information
     * of the operating system.
     *
     * @param systemDescription the system information object
     * @return the message attribute
     * @throws ValidationException if attribute creation fails
     */
    private PaAttribute getProductInformation(final UTSNAME systemDescription)
            throws ValidationException {
        // RFC 5792 Vendor ID unknown = 0 => Product ID = 0
        return PaAttributeFactoryIetf.createProductInformation(0, 0,
                new String(systemDescription.version).trim());
    }

    @Override
    public synchronized List<ImAttribute> lastCall(
            final ImSessionContext context) {

        LOGGER.info("Last call received.");
        return new ArrayList<>(0);
    }
}
