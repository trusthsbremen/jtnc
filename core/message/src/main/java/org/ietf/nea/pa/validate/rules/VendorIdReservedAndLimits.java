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

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that checks if the attribute vendor ID is reserved or not in range.
 *
 */
public abstract class VendorIdReservedAndLimits {
    /**
     * Private constructor should never be invoked.
     */
    private VendorIdReservedAndLimits() {
        throw new AssertionError();
    }

    /**
     * Checks if the attribute vendor ID is reserved or not in range.
     * @param vendorId the vendor ID
     * @throws RuleException if check fails
     */
    public static void check(final long vendorId) throws RuleException {
        if (vendorId == IETFConstants.IETF_VENDOR_ID_RESERVED) {
            throw new RuleException("Vendor ID is set to reserved value.",
                    false,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.VENDOR_ID_RESERVED.id(), vendorId);

        }
        if (vendorId > IETFConstants.IETF_MAX_VENDOR_ID) {
            throw new RuleException("Vendor ID is to large.", false,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.VALUE_TO_LARGE.id(), vendorId);
        }

        if (vendorId < 0) {
            throw new RuleException("Vendor ID cannot be negativ.", false,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.NEGATIV_UNSIGNED.id(), vendorId);
        }
    }

}
