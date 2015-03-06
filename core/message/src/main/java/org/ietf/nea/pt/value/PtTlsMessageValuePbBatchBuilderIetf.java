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
package org.ietf.nea.pt.value;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

/**
 * Builder to build a transport TNCCS batch message value compliant to RFC 6876.
 * It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PtTlsMessageValuePbBatchBuilderIetf implements
        PtTlsMessageValuePbBatchBuilder {

    private long length;
    private ByteBuffer tnccsData; // variable string

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>Batch: null</li>
     * </ul>
     */
    public PtTlsMessageValuePbBatchBuilderIetf() {
        this.length = 0;
        this.tnccsData = null;
    }

    @Override
    public PtTlsMessageValuePbBatchBuilder setTnccsData(final ByteBuffer data)
            throws RuleException {
        // no checks necessary
        if (data != null) {
            this.tnccsData = data;
            this.length = data.bytesWritten();
        }

        return this;
    }

    @Override
    public PtTlsMessageValuePbBatch toObject() {
        if (this.tnccsData == null) {
            throw new IllegalStateException("The batch data has to be set.");
        }
        return new PtTlsMessageValuePbBatch(this.length, tnccsData);
    }

    @Override
    public PtTlsMessageValuePbBatchBuilder newInstance() {

        return new PtTlsMessageValuePbBatchBuilderIetf();
    }

}
