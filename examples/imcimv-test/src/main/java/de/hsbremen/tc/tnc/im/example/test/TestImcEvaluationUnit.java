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
import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentTypeEnum;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImcEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Example file IMC evaluation unit, which uses
 * a hash sum to monitor file changes.
 *
 *
 */
public class TestImcEvaluationUnit extends AbstractImcEvaluationUnitIetf {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TestImcEvaluationUnit.class);

    public static final long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
    public static final long TYPE = PaComponentTypeEnum.IETF_PA_TESTING.id();

    private final String value;

    /**
     * Create a the evaluation unit with the given handshake retry listener.
     *
     * @param messageDigestIdentifier the algorithm identifier (e.g. SHA-1)
     * @param filePath the path to the file
     * @param globalHandshakeRetryListener the global handshake retry listener.
     */
    public TestImcEvaluationUnit(final String value,
            final GlobalHandshakeRetryListener globalHandshakeRetryListener) {
        super(globalHandshakeRetryListener);
        this.value = value;
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

        List<ImAttribute> attributes = new ArrayList<>();

        try {
            PaAttribute file = this.getValue();
            attributes.add(file);
        } catch (ValidationException e) {
            LOGGER.error("Attribute clould not be created.", e);
        }

        return attributes;
    }
    
    @Override
    public void notifyConnectionChange(ImSessionContext context) {
        LOGGER.debug("Connection change notification received.");
        
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

        List<AttributeReferenceEntry> references = value.getReferences();
        for (AttributeReferenceEntry attributeReference : references) {
            try {
                if (attributeReference.getVendorId() == 0) {
                    if (attributeReference.getType()
                            == PaAttributeTypeEnum.IETF_PA_TESTING
                            .id()) {

                        attributeList.add(this.getValue());
                    }
                }
            } catch (ValidationException | NumberFormatException e) {
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
        LOGGER.info("Remediation instructions were received. ");

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
     * Creates a message attribute containing the hash sum of the
     * monitored file.
     *
     * @return the message attribute
     * @throws ValidationException if attribute creation fails
     */
    private PaAttribute getValue() throws ValidationException {
        return PaAttributeFactoryIetf.createTestValue(this.value);
    }

    @Override
    public synchronized List<ImAttribute> lastCall(
            final ImSessionContext context) {

        LOGGER.info("Last call received.");
        return new ArrayList<>(0);
    }
}
