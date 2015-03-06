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
public abstract class BatchDirectionAndType {
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
