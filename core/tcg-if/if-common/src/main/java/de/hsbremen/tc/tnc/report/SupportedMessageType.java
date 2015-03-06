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
package de.hsbremen.tc.tnc.report;

/**
 * Entity which holds a qualified message type using vendor ID and type ID.
 *
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
