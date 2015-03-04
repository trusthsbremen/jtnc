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
package org.ietf.nea.pa.message;

import de.hsbremen.tc.tnc.message.m.message.ImMessageHeader;

/**
 * IETF RFC 5792 integrity measurement message header.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PaMessageHeader implements ImMessageHeader {

    private final short version; // 8 bit(s)
    private final long identifier; // 32 bit(s)
    private long length; // not official part

    /**
     * Creates the header with the given values.
     * @param version the message format version
     * @param identifier the message identifier
     * @param length the message length
     */
    PaMessageHeader(final short version,
            final long identifier, final long length) {
        this.version = version;
        this.identifier = identifier;
        this.length = length;
    }

    @Override
    public short getVersion() {
        return this.version;
    }

    @Override
    public long getIdentifier() {
        return this.identifier;
    }

    @Override
    public long getLength() {
        return length;
    }

    /**
     * Sets the length value of the header.
     * @param length the length of the message
     */
    public void setLength(final long length) {
        this.length = length;
    }

}
