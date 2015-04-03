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
package org.ietf.nea.pb.validate.rules;

import java.util.List;

import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Rule, that checks if batch is result batch and contains assessment result.
 *
 */
public final class BatchResultWithoutMessageAssessmentResult {
    /**
     * Private constructor should never be invoked.
     */
    private BatchResultWithoutMessageAssessmentResult() {
        throw new AssertionError();
    }
    /**
     * Checks if batch is result batch and contains assessment result.
     * @param type the batch type
     * @param messages the list of messages
     * @throws RuleException if check fails
     */
    public static void check(final PbBatchTypeEnum type,
            final List<? super PbMessage> messages) throws RuleException {
        if (type != PbBatchTypeEnum.RESULT) {
            return;
        }
        if (messages != null) {
            for (Object pbMessage : messages) {
                if (((PbMessage) pbMessage).getHeader().getVendorId()
                        == IETFConstants.IETF_PEN_VENDORID
                        &&
                    ((PbMessage) pbMessage).getHeader().getMessageType()
                        == PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT
                                .id()) {
                    return;
                }
            }
        }
        throw new RuleException("The batch of type " + type.toString()
                + " must contain a message with vendor id "
                + IETFConstants.IETF_PEN_VENDORID + " and message type "
                + PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id() + ".", true,
                PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                PbErrorCauseEnum.BATCH_RESULT_NO_ASSESSMENT_RESULT.id(),
                type.toString(), IETFConstants.IETF_PEN_VENDORID,
                PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.toString());
    }
}
