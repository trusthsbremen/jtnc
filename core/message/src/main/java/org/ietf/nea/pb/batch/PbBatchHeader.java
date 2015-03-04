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

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatchHeader;

/**
 * IETF RFC 5793 TNCCS batch header.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PbBatchHeader implements TnccsBatchHeader {

    private final short version; // 8 bit(s)
    private final PbBatchDirectionalityEnum directionality; // 1 bit(s)
    private final PbBatchTypeEnum type; // 4 bit(s)

    private final long length;

    /**
     * Creates the header with the given values.
     * @param version the batch format version
     * @param directionality the sending direction (e.g to server or client)
     * @param type the batch type (e.g. server retry, ...)
     * @param length the batch length
     */
    PbBatchHeader(final short version,
            final PbBatchDirectionalityEnum directionality,
            final PbBatchTypeEnum type, final long length) {
        this.version = version;
        this.directionality = directionality;
        this.type = type;
        this.length = length;
    }

    @Override
    public short getVersion() {
        return version;
    }

    /**
     * @return the directionality
     */
    public PbBatchDirectionalityEnum getDirectionality() {
        return directionality;
    }

    /**
     * @return the type
     */
    public PbBatchTypeEnum getType() {
        return type;
    }

    /**
     * @return the length
     */
    public long getLength() {
        return length;
    }
}
