/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
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
