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
package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Rule, that checks if the batch type and the batch direction correlate.
 *
 */
public final class BatchDirectionAndType {
    /**
     * Private constructor should never be invoked.
     */
    private BatchDirectionAndType() {
        throw new AssertionError();
    }
    /**
     * Checks if the batch type and the batch direction correlate.
     * @param direction the batch direction
     * @param type the batch type
     * @throws RuleException if check fails
     */
    public static void check(final PbBatchDirectionalityEnum direction,
            final PbBatchTypeEnum type) throws RuleException {
        NotNull.check("Direction cannot be null.", direction);
        NotNull.check("Type cannot be null.", type);

        if (direction == PbBatchDirectionalityEnum.TO_PBS) {
            if (type == PbBatchTypeEnum.RESULT || type == PbBatchTypeEnum.SDATA
                    || type == PbBatchTypeEnum.SRETRY) {
                throw new RuleException("The batch type " + type.toString()
                        + " is not expected in the direction "
                        + direction.toString() + ".", true,
                        PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE
                                .code(),
                        PbErrorCauseEnum.BATCH_DIRECTION_OR_TYPE_UNEXPECTED
                                .id(), type.toString(), direction.toString());
            }
        } else if (direction == PbBatchDirectionalityEnum.TO_PBC) {
            if (type == PbBatchTypeEnum.CDATA
                    || type == PbBatchTypeEnum.CRETRY) {
                throw new RuleException("The batch type " + type.toString()
                        + " is not expected in the direction "
                        + direction.toString() + ".", true,
                        PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE
                                .code(),
                        PbErrorCauseEnum.BATCH_DIRECTION_OR_TYPE_UNEXPECTED
                                .id(), type.toString(), direction.toString());
            }
        }
    }
}
