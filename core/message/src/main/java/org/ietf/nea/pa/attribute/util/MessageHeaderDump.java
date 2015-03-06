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
package org.ietf.nea.pa.attribute.util;

import java.util.Arrays;

/**
 * Helper object that contains a parsed IETF RFC 5792 message header dump
 * including any reserved value fields for error handling.
 *
 *
 */
public class MessageHeaderDump {

    private final short version;
    private final byte[] reserved;
    private final long identifier;

    /**
     * Creates the helper object with the given message header values.
     * @param version the message format version
     * @param reserved the reserved message header part
     * @param identifier the message identifier
     */
    public MessageHeaderDump(final short version, final byte[] reserved,
            final long identifier) {
        this.version = version;
        this.reserved = (reserved != null) ? reserved : new byte[0];
        this.identifier = identifier;
    }

    /**
     * Returns the message format version.
     * @return the message format version
     */
    public short getVersion() {
        return this.version;
    }

    /**
     * Returns the reserved header part.
     * @return the reserved part
     */
    public byte[] getReserved() {
        return Arrays.copyOf(this.reserved, this.reserved.length);
    }

    /**
     * Returns the message identifier.
     * @return the identifier
     */
    public long getIdentifier() {
        return this.identifier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (int) (this.identifier ^ (this.identifier >>> 32));
        result = prime * result + Arrays.hashCode(this.reserved);
        result = prime * result + this.version;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MessageHeaderDump other = (MessageHeaderDump) obj;
        if (this.identifier != other.identifier) {
            return false;
        }
        if (!Arrays.equals(this.reserved, other.reserved)) {
            return false;
        }
        if (this.version != other.version) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RawMessageHeader [version=" + this.version + ", reserved="
                + Arrays.toString(this.reserved) + ", identifier="
                + this.identifier + "]";
    }

}
