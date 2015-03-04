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
package org.ietf.nea.pt.value.util;

import java.nio.charset.Charset;

/**
 * Entry object describing a SASL mechanism.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class SaslMechanismEntry {

    private final byte nameLength; // 5 bit(s)
    private final String name; // variable length 1-20 byte(s)

    /**
     * Creates an entry object with the given SASL mechanism name.
     *
     * @param  name the SASL mechanism name
     */
    public SaslMechanismEntry(final String name) {
        this.name = name;
        this.nameLength =
                (byte) name.getBytes(Charset.forName("US-ASCII")).length;
    }

    /**
     * Returns the mechanism name length.
     * @return the name length
     */
    public byte getNameLength() {
        return this.nameLength;
    }

    /**
     * Returns the mechanism name.
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + this.nameLength;
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
        SaslMechanismEntry other = (SaslMechanismEntry) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (this.nameLength != other.nameLength) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SaslMechanismEntry [nameLength=" + this.nameLength + ", name="
                + this.name + "]";
    }
}
