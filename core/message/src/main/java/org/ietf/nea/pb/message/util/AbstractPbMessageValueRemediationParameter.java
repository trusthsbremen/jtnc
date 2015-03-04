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
package org.ietf.nea.pb.message.util;

import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageSubValue;

/**
 * Generic IETF RFC 5793 remediation parameter base. Especially important for
 * inheritance.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class AbstractPbMessageValueRemediationParameter implements
        TnccsMessageSubValue {

    private final long length;

    /**
     * Creates the base with the given value length.
     *
     * @param length the value length
     */
    AbstractPbMessageValueRemediationParameter(final long length) {
        this.length = length;
    }

    /**
     * Returns the value length.
     *
     * @return the length
     */
    public long getLength() {
        return this.length;
    }

}