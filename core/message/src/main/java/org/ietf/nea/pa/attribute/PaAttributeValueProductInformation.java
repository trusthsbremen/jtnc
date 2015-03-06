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
package org.ietf.nea.pa.attribute;

/**
 * IETF RFC 5792 integrity measurement product information attribute value.
 *
 *
 */
public class PaAttributeValueProductInformation extends
        AbstractPaAttributeValue {

    private final long vendorId; // 24 bit(s)
    private final int productId; // 16 bit(s)
    private final String name; // variable length

    /**
     * Creates the attribute value with the given values.
     * @param length the value length
     * @param vendorId the product vendor ID
     * @param productId the product ID
     * @param name the product name
     */
    PaAttributeValueProductInformation(final long length, final long vendorId,
            final int productId, final String name) {
        super(length);

        this.vendorId = vendorId;
        this.productId = productId;
        this.name = name;
    }

    /**
     * Returns the product vendor ID.
     * @return the vendor ID
     */
    public long getVendorId() {
        return this.vendorId;
    }

    /**
     * Returns the product ID.
     * @return the product ID
     */
    public int getProductId() {
        return this.productId;
    }

    /**
     * Returns the product name.
     * @return the product name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "PaAttributeValueProductInformation [vendorId=" + this.vendorId
                + ", productId=" + this.productId + ", name=" + this.name + "]";
    }
}
