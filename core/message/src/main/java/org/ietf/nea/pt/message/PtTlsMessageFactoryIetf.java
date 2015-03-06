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
package org.ietf.nea.pt.message;

import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;
import org.ietf.nea.pt.value.AbstractPtTlsMessageValue;
import org.ietf.nea.pt.value.PtTlsMessageValueFactoryIetf;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Factory utility to create an IETF RFC 6876 compliant transport message.
 *
 *
 */
public abstract class PtTlsMessageFactoryIetf {

    private static final long VENDORID = IETFConstants.IETF_PEN_VENDORID;

    /**
     * Private constructor should never be invoked.
     */
    private PtTlsMessageFactoryIetf() {
        throw new AssertionError();
    }

    /**
     * Creates an experimental transport message with the
     * given content.
     * @param identifier the monotonically increasing message identifier
     * @param content the message content
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public PtTlsMessage createExperimental(final long identifier,
            final String content) throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_EXERIMENTAL.id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf.createExperimentalValue(content));
    }
    /**
     * Creates transport message version request.
     * @param identifier the monotonically increasing message identifier
     * @param minVersion the minimal version supported
     * @param maxVersion the maximal version supported
     * @param preferredVersion the preferred version
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createVersionRequest(final long identifier,
            final short minVersion, final short maxVersion,
            final short preferredVersion) throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_REQUEST
                .id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf.createVersionRequestValue(
                        minVersion, maxVersion, preferredVersion));

    }

    /**
     * Creates transport message version response.
     * @param identifier the monotonically increasing message identifier
     * @param selectedVersion the selected version
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createVersionResponse(final long identifier,
            final short selectedVersion) throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_RESPONSE
                .id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf
                        .createVersionResponseValue(selectedVersion));
    }

    /**
     * Creates transport message containing SASL mechanisms.
     * @param identifier the monotonically increasing message identifier
     * @param mechanisms the supported SASL mechanisms
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createSaslMechanisms(final long identifier,
            final SaslMechanismEntry... mechanisms) throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISMS
                .id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf
                        .createSaslMechanismsValue(mechanisms));
    }

    /**
     * Creates transport message with a SASL mechanism selection.
     * @param identifier the monotonically increasing message identifier
     * @param mechanism the selected SASL mechanism
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createSaslMechanismSelection(
            final long identifier, final SaslMechanismEntry mechanism)
            throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION
                .id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf
                        .createSaslMechanismSelectionValue(mechanism));
    }

    /**
     * Creates transport message with a SASL mechanism selection and initial
     * SASL data.
     * @param identifier the monotonically increasing message identifier
     * @param mechanism the selected SASL mechanism
     * @param initialData the initial SASL data
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createSaslMechanismSelection(
            final long identifier, final SaslMechanismEntry mechanism,
            final byte[] initialData) throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION
                .id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf.createSaslMechanismSelectionValue(
                        mechanism, initialData));

    }

    /**
     * Creates transport message with a SASL authentication data.
     * @param identifier the monotonically increasing message identifier
     * @param authenticationData the SASL authentication data
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createSaslAuthenticationData(
            final long identifier, final byte[] authenticationData)
            throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_AUTHENTICATION_DATA
                .id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf
                        .createSaslAuthenticationDataValue(authenticationData));

    }

    /**
     * Creates transport message with a SASL authentication result.
     * @param identifier the monotonically increasing message identifier
     * @param result the SASL authentication result
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createSaslResult(final long identifier,
            final PtTlsSaslResultEnum result) throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf.createSaslResultValue(result));

    }

    /**
     * Creates transport message with a SASL authentication result and
     * additional SASL result data.
     * @param identifier the monotonically increasing message identifier
     * @param result the SASL authentication result
     * @param resultData the additional result data
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createSaslResult(final long identifier,
            final PtTlsSaslResultEnum result, final byte[] resultData)
            throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf.createSaslResultValue(result,
                        resultData));

    }

    /**
     * Creates transport message with an error and the message header object
     * from the erroneous message.
     * @param identifier the monotonically increasing message identifier
     * @param errorVendorId the error vendor ID
     * @param errorCode the error code describing the error
     * @param messageHeader the message header object of the erroneous message
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createError(final long identifier,
            final long errorVendorId, final long errorCode,
            final PtTlsMessageHeader messageHeader) throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.id();

        ByteBuffer buffer = new DefaultByteBuffer(
                PtTlsMessageTlvFixedLengthEnum.MESSAGE.length());

        /* reserved 8 bit(s) */
        buffer.writeByte((byte) 0);
        /* vendor ID 24 bit(s) */
        buffer.writeDigits(messageHeader.getVendorId(), (byte) 3);
        /* message Type 32 bit(s) */
        buffer.writeUnsignedInt(messageHeader.getMessageType());
        /* message length 32 bit(s) */
        buffer.writeUnsignedInt(messageHeader.getLength());
        /* message identifier 32 bit(s) */
        buffer.writeUnsignedInt(messageHeader.getIdentifier());

        byte[] messageCopy = buffer.read(PtTlsMessageTlvFixedLengthEnum.MESSAGE
                .length());

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf.createErrorValue(errorVendorId,
                        errorCode, messageCopy));

    }

    /**
     * Creates transport message with an error and the byte copy of the
     * the erroneous message.
     * @param identifier the monotonically increasing message identifier
     * @param errorVendorId the error vendor ID
     * @param errorCode the error code describing the error
     * @param messageCopy the byte copy of the erroneous message
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createError(final long identifier,
            final long errorVendorId, final long errorCode,
            final byte[] messageCopy) throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf.createErrorValue(errorVendorId,
                        errorCode, messageCopy));

    }

    /**
     * Creates transport message with a TNCCS batch.
     * @param identifier the monotonically increasing message identifier
     * @param tnccsData the serialized TNCCS batch
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PtTlsMessage createPbBatch(final long identifier,
            final ByteBuffer tnccsData) throws ValidationException {
        long type = PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.id();

        return createMessage(identifier, type,
                PtTlsMessageValueFactoryIetf.createPbBatchValue(tnccsData));

    }

    /**
     * Creates a transport message with the given identifier and type containing
     * the given message value.
     * @param identifier the monotonically increasing message identifier
     * @param type the message type
     * @param value the message value
     * @return an IETF RFC 6876 compliant transport message
     * @throws ValidationException if creation fails because of invalid values
     */
    private static PtTlsMessage createMessage(final long identifier,
            final long type, final AbstractPtTlsMessageValue value)
            throws ValidationException {
        NotNull.check("Value cannot be null.", value);

        if (identifier > 0xFFFFFFFFL) {
            throw new IllegalArgumentException(
                    "Identifier to large. Identifier "
                    + "must be in range from 0 to " + 0xFFFFFFFFL);
        }

        PtTlsMessageHeaderBuilderIetf mBuilder =
                new PtTlsMessageHeaderBuilderIetf();
        try {

            mBuilder.setVendorId(VENDORID);
            mBuilder.setType(type);
            mBuilder.setLength(PtTlsMessageTlvFixedLengthEnum.MESSAGE.length()
                    + value.getLength());
            mBuilder.setIdentifier(identifier);

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e,
                    ValidationException.OFFSET_NOT_SET);
        }

        PtTlsMessage message = new PtTlsMessage(mBuilder.toObject(), value);

        return message;

    }
}
