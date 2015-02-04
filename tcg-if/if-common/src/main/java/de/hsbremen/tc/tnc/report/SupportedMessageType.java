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
package de.hsbremen.tc.tnc.report;

/**
 * Entity which holds a qualified message type using vendor ID and type ID.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class SupportedMessageType implements Comparable<SupportedMessageType> {

    private final long vendorId;
    private final long typeId;

    /**
     * Creates a message type with the given
     * vendor ID and type ID.
     *
     * @param vendorId the vendor ID
     * @param typeId the type ID
     */
    SupportedMessageType(final long vendorId, final long typeId) {

        this.vendorId = vendorId;
        this.typeId = typeId;
    }

    /**
     * Returns the contained vendor ID.
     *
     * @return the vendor ID
     */
    public long getVendorId() {
        return vendorId;
    }

    /**
     * Returns the contained type ID.
     *
     * @return the type ID
     */
    public long getType() {
        return typeId;
    }

    @Override
    public int compareTo(final SupportedMessageType o) {
        if (this.getVendorId() < o.getVendorId()) {
            return -1;
        }
        if (this.getVendorId() > o.getVendorId()) {
            return 1;
        }
        if (this.getVendorId() == o.getVendorId()) {
            if (this.getType() < o.getType()) {
                return -1;
            }
            if (this.getType() > o.getType()) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "SupportedMessageType [vendorId=" + this.vendorId + ", type="
                + this.typeId + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        final int shiftFactor = 32;
        int result = 1;
        result = prime * result
                + (int) (this.typeId ^ (this.typeId >>> shiftFactor));
        result = prime * result
                + (int) (this.vendorId ^ (this.vendorId >>> shiftFactor));
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
        SupportedMessageType other = (SupportedMessageType) obj;
        if (this.typeId != other.typeId) {
            return false;
        }
        if (this.vendorId != other.vendorId) {
            return false;
        }
        return true;
    }

}
