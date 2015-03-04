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

/**
 * Entry object describing an attribute type.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class AttributeReferenceEntry {

    private final long vendorId; // 24 bit(s)
    private final long type; // 32 bit(s)

    /**
     * Creates an entry object with the given attribute vendor and type
     * ID.
     *
     * @param vendorId the vendor ID
     * @param type the attribute type ID
     */
    public AttributeReferenceEntry(final long vendorId, final long type) {
        this.vendorId = vendorId;
        this.type = type;
    }

    /**
     * Returns the vendor ID.
     * @return the vendor ID
     */
    public long getVendorId() {
        return this.vendorId;
    }

    /**
     * Returns the type ID.
     * @return the type ID
     */
    public long getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "AttributeReferenceEntry [vendorId=" + this.vendorId + ", type="
                + this.type + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.type ^ (this.type >>> 32));
        result = prime * result
                + (int) (this.vendorId ^ (this.vendorId >>> 32));
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
        AttributeReferenceEntry other = (AttributeReferenceEntry) obj;
        if (this.type != other.type) {
            return false;
        }
        if (this.vendorId != other.vendorId) {
            return false;
        }
        return true;
    }

}
