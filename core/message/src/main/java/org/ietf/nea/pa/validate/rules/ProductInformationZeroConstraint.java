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
package org.ietf.nea.pa.validate.rules;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that checks if product ID is zero if vendor ID is zero.
 * @author Carl-Heinz Genzel
 *
 */
public abstract class ProductInformationZeroConstraint {

    /**
     * Private constructor should never be invoked.
     */
    private ProductInformationZeroConstraint() {
        throw new AssertionError();
    }

    /**
     * Checks if product ID is zero if vendor ID is zero.
     * @param productVendorId the vendor ID
     * @param productId the product ID
     * @throws RuleException if check fails
     */
    public static void check(final long productVendorId, final int productId)
            throws RuleException {
        if (productVendorId == 0 && productId != 0) {
            throw new RuleException(
                    "Product information attribute has"
                    + " an invalid value constellation, having a vendor id of "
                            + productVendorId
                            + " and a product id of "
                            + productId + ".", false,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.INVALID_PRODUCT_ID.id(),
                    Long.toString(productVendorId), productId);
        }
    }
}
