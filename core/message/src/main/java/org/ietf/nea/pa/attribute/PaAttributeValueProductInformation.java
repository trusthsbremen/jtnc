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
package org.ietf.nea.pa.attribute;

/**
 * IETF RFC 5792 integrity measurement product information attribute value.
 *
 * @author Carl-Heinz Genzel
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
