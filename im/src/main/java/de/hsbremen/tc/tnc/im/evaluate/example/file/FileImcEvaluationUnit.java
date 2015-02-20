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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.ietf.nea.pa.attribute.PaAttribute;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.PaAttributeValueError;
import org.ietf.nea.pa.attribute.PaAttributeValueRemediationParameters;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReference;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImcEvaluationUnitIetf;
import de.hsbremen.tc.tnc.im.evaluate.enums.PaComponentTypeEnum;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Example file IMC evaluation unit, which uses
 * a hash sum to monitor file changes.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class FileImcEvaluationUnit extends AbstractImcEvaluationUnitIetf {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FileImcEvaluationUnit.class);

    public static final long VENDOR_ID = IETFConstants.IETF_PEN_VENDORID;
    public static final long TYPE = PaComponentTypeEnum.IETF_PA_TESTING.id();

    private final String filePath;
    private final String messageDigestIdentifier;

    /**
     * Create a the evaluation unit with the given handshake retry listener.
     *
     * @param messageDigestIdentifier the algorithm identifier (e.g. SHA-1)
     * @param filePath the path to the file
     * @param globalHandshakeRetryListener the global handshake retry listener.
     */
    public FileImcEvaluationUnit(final String messageDigestIdentifier,
            final String filePath,
            final GlobalHandshakeRetryListener globalHandshakeRetryListener) {
        super(globalHandshakeRetryListener);
        this.filePath = filePath;
        this.messageDigestIdentifier = messageDigestIdentifier;
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
            PaAttribute file = this.getFileHash();
            attributes.add(file);
        } catch (ValidationException e) {
            LOGGER.error("String version clould not be created.", e);
        }

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

        List<AttributeReference> references = value.getReferences();
        for (AttributeReference attributeReference : references) {
            try {
                if (attributeReference.getVendorId() == 0) {
                    if (attributeReference.getType()
                            == PaAttributeTypeEnum.IETF_PA_TESTING
                            .attributeType()) {

                        attributeList.add(this.getFileHash());
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
                + " - (# " + value.getResult().number() + ")");
        return new ArrayList<>(0);
    }

    /**
     * Creates a message attribute containing the hash sum of the
     * monitored file.
     *
     * @return the message attribute
     * @throws ValidationException if attribute creation fails
     */
    private PaAttribute getFileHash() throws ValidationException {
        String content = "";

        byte[] newDigest = null;
        try {
            MessageDigest md = MessageDigest
                    .getInstance(this.messageDigestIdentifier);
            DigestInputStream dis = null;
            File f = new File(this.filePath);
            if (f.exists() && f.canRead()) {
                try {
                    dis = new DigestInputStream(new BufferedInputStream(
                            new FileInputStream(f)), md);

                    while ((dis.read()) != -1) {
                        // read all
                    }
                    newDigest = md.digest();
                } catch (IOException e) {
                    LOGGER.error(
                            "Could not check file because of an error.", e);
                } finally {
                    if (dis != null) {
                        try {
                            dis.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Could not check file because of an error.", e);
        }

        if (newDigest != null) {
            content = DatatypeConverter.printBase64Binary(newDigest);
        }

        return PaAttributeFactoryIetf.createTestValue(content);
    }

    @Override
    public synchronized List<ImAttribute> lastCall(
            final ImSessionContext context) {

        LOGGER.info("Last call received.");
        return new ArrayList<>(0);
    }
}
