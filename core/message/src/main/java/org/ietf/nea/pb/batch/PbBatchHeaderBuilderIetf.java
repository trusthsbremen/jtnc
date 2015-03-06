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

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;
import org.ietf.nea.pb.validate.rules.BatchDirectionAndType;
import org.ietf.nea.pb.validate.rules.BatchDirectionality;
import org.ietf.nea.pb.validate.rules.BatchType;
import org.ietf.nea.pb.validate.rules.BatchVersion;
import org.ietf.nea.pb.validate.rules.CommonLengthLimits;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build a TNCCS batch header compliant to RFC 5793. It
 * evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PbBatchHeaderBuilderIetf implements PbBatchHeaderBuilder {

    private static final byte SUPPORTED_VERSION = 2;

    private short version;
    private PbBatchTypeEnum type;
    private PbBatchDirectionalityEnum direction;
    private long batchLength;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Version: 2</li>
     * <li>Type: CLOSE</li>
     * <li>Direction: to server</li>
     * <li>Length: Header length only</li>
     * </ul>
     */
    public PbBatchHeaderBuilderIetf() {
        this.version = SUPPORTED_VERSION;
        this.type = PbBatchTypeEnum.CLOSE;
        this.direction = PbBatchDirectionalityEnum.TO_PBS;
        this.batchLength = PbMessageTlvFixedLengthEnum.BATCH.length();
    }

    @Override
    public PbBatchHeaderBuilder setVersion(final short version)
            throws RuleException {
        BatchVersion.check(version, SUPPORTED_VERSION);

        this.version = version;

        return this;
    }

    @Override
    public PbBatchHeaderBuilder setDirection(final byte direction)
            throws RuleException {

        BatchDirectionality.check(direction);
        PbBatchDirectionalityEnum tempDir = PbBatchDirectionalityEnum
                .fromDirectionalityBit(direction);

        if (type != null) {
            BatchDirectionAndType.check(tempDir, type);
        }

        this.direction = tempDir;

        return this;
    }

    @Override
    public PbBatchHeaderBuilder setType(final byte type)
            throws RuleException {

        BatchType.check(type);

        PbBatchTypeEnum tempType = PbBatchTypeEnum.fromId(type);

        if (direction != null) {
            BatchDirectionAndType.check(direction, tempType);
        }

        this.type = tempType;

        return this;
    }

    @Override
    public PbBatchHeaderBuilder setLength(final long length)
            throws RuleException {

        CommonLengthLimits.check(length);

        this.batchLength = length;

        return this;
    }

    @Override
    public PbBatchHeader toObject() {

        PbBatchHeader batch = new PbBatchHeader(version, direction, type,
                batchLength);

        return batch;
    }

    @Override
    public PbBatchHeaderBuilder newInstance() {
        return new PbBatchHeaderBuilderIetf();
    }

}
