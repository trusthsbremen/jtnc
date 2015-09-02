/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.ietf.nea.pa.attribute.util;

/**
 * Entry object describing an attribute type.
 *
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
