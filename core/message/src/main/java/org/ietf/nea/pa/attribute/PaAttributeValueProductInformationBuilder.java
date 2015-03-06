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

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValueBuilder;

/**
 * Generic builder to build an integrity measurement product information
 * attribute value compliant to RFC 5792. It can be used in a fluent way.
 *
 *
 */
public interface PaAttributeValueProductInformationBuilder extends
        ImAttributeValueBuilder {

    /**
     * Sets the product vendor ID.
     *
     * @param vendorId the product vendor ID
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PaAttributeValueProductInformationBuilder setVendorId(long vendorId)
            throws RuleException;

    /**
     * Sets the product ID.
     *
     * @param productId the product ID
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PaAttributeValueProductInformationBuilder setProductId(int productId)
            throws RuleException;

    /**
     * Sets the product name.
     *
     * @param name the product name
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PaAttributeValueProductInformationBuilder setName(String name)
            throws RuleException;

}
