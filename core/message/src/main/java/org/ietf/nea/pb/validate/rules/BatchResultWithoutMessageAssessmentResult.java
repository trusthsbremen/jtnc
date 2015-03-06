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
public abstract class BatchResultWithoutMessageAssessmentResult {
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
