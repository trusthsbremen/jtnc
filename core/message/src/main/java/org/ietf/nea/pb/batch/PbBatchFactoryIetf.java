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
package org.ietf.nea.pb.batch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.BatchResultWithoutMessageAssessmentResult;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

/**
 * Factory utility to create a IETF RFC 5793 compliant TNCCS message batch.
 *
 *
 */
public abstract class PbBatchFactoryIetf {

    /**
     * Private constructor should never be invoked.
     */
    private PbBatchFactoryIetf() {
        throw new AssertionError();
    }

    /**
     * Creates a server data batch containing the given messages.
     * @param messages the contained TNCCS messages
     * @return an IETF RFC 5793 compliant TNCCS message batch
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbBatch createServerData(
            final List<? extends TnccsMessage> messages)
            throws ValidationException {
        return createBatch(PbBatchDirectionalityEnum.TO_PBC,
                PbBatchTypeEnum.SDATA, messages);
    }

    /**
     * Creates a result batch containing the given messages.
     * @param messages the contained TNCCS messages
     * @return an IETF RFC 5793 compliant TNCCS message batch
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbBatch createResult(
            final List<? extends TnccsMessage> messages)
            throws ValidationException {
        return createBatch(PbBatchDirectionalityEnum.TO_PBC,
                PbBatchTypeEnum.RESULT, messages);
    }

    /**
     * Creates a server retry batch containing the given messages.
     * @param messages the contained TNCCS messages
     * @return an IETF RFC 5793 compliant TNCCS message batch
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbBatch createServerRetry(
            final List<? extends TnccsMessage> messages)
                    throws ValidationException {
        return createBatch(PbBatchDirectionalityEnum.TO_PBC,
                PbBatchTypeEnum.SRETRY, messages);
    }

    /**
     * Creates a server close batch containing the given messages.
     * @param messages the contained TNCCS messages
     * @return an IETF RFC 5793 compliant TNCCS message batch
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbBatch createServerClose(
            final List<? extends TnccsMessage> messages)
            throws ValidationException {
        return createBatch(PbBatchDirectionalityEnum.TO_PBC,
                PbBatchTypeEnum.CLOSE, messages);
    }

    /**
     * Creates a client retry batch containing the given messages.
     * @param messages the contained TNCCS messages
     * @return an IETF RFC 5793 compliant TNCCS message batch
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbBatch createClientRetry(
            final List<? extends TnccsMessage> messages)
            throws ValidationException {
        return createBatch(PbBatchDirectionalityEnum.TO_PBS,
                PbBatchTypeEnum.CRETRY, messages);
    }

    /**
     * Creates a client data batch containing the given messages.
     * @param messages the contained TNCCS messages
     * @return an IETF RFC 5793 compliant TNCCS message batch
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbBatch createClientData(
            final List<? extends TnccsMessage> messages)
            throws ValidationException {
        return createBatch(PbBatchDirectionalityEnum.TO_PBS,
                PbBatchTypeEnum.CDATA, messages);
    }

    /**
     * Creates a client close batch containing the given messages.
     * @param messages the contained TNCCS messages
     * @return an IETF RFC 5793 compliant TNCCS message batch
     * @throws ValidationException if creation fails because of invalid values
     */
    public static PbBatch createClientClose(
            final List<? extends TnccsMessage> messages)
            throws ValidationException {
        return createBatch(PbBatchDirectionalityEnum.TO_PBS,
                PbBatchTypeEnum.CLOSE, messages);
    }

    /**
     * Creates a batch with the given type and direction containing the
     * given messages.
     * @param direction the batch sending direction (e.g. to server or client)
     * @param type the batch type (e.g. server retry, close, ...)
     * @param messages the contained TNCCS messages
     * @return an IETF RFC 5793 compliant TNCCS message batch
     * @throws ValidationException if creation fails because of invalid values
     */
    private static PbBatch createBatch(
            final PbBatchDirectionalityEnum direction,
            final PbBatchTypeEnum type,
            final List<? extends TnccsMessage> messages)
            throws ValidationException {
        List<? extends TnccsMessage> msgs = (messages != null) ? messages
                : new ArrayList<TnccsMessage>();

        PbBatchHeaderBuilderIetf builder = new PbBatchHeaderBuilderIetf();
        List<PbMessage> filteredMsgs = new LinkedList<>();

        try {
            builder.setDirection(direction.toDirectionalityBit());
            builder.setType(type.id());

            long l = 0;
            for (TnccsMessage message : msgs) {
                if (message instanceof PbMessage) {
                    PbMessage pbMessage = (PbMessage) message;
                    l += pbMessage.getHeader().getLength();
                    filteredMsgs.add(pbMessage);

                } else {
                    throw new IllegalArgumentException("Message type "
                            + message.getClass().getCanonicalName()
                            + " not supported. TnccsMessage must be of type "
                            + PbMessage.class.getCanonicalName() + ".");
                }
            }

            builder.setLength(l + PbMessageTlvFixedLengthEnum.BATCH.length());

            BatchResultWithoutMessageAssessmentResult.check(type, filteredMsgs);

        } catch (RuleException e) {
            throw new ValidationException(e.getMessage(), e,
                    ValidationException.OFFSET_NOT_SET);
        }

        PbBatch batch = new PbBatch(builder.toObject(), filteredMsgs);

        return batch;
    }

}
