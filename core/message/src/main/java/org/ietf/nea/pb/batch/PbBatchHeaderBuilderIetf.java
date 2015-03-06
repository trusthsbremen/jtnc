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
