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

import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeTypeEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Rule, that checks if the list of attribute references contains references
 * to forbidden attributes.
 *
 *
 */
public abstract class RequestingForbiddenAttributes {
    /**
     * Private constructor should never be invoked.
     */
    private RequestingForbiddenAttributes() {
        throw new AssertionError();
    }

    /**
     * Checks if the list of attribute references contains references to
     * forbidden attributes.
     *
     * @param attributes the list of attribute references
     * @throws RuleException if check fails
     */
    public static void check(final List<AttributeReferenceEntry> attributes)
            throws RuleException {
        if (attributes != null) {
            for (AttributeReferenceEntry ref : attributes) {
                if (ref.getVendorId() == IETFConstants.IETF_PEN_VENDORID) {
                    if (ref.getType() == PaAttributeTypeEnum
                            .IETF_PA_ATTRIBUTE_REQUEST.id()) {
                        throw new RuleException(
                                "The attribute request must not request"
                                        + " an attribute with vendor id "
                                        + IETFConstants.IETF_PEN_VENDORID
                                        + " and attribute type "
                                        + PaAttributeTypeEnum
                                            .IETF_PA_ATTRIBUTE_REQUEST.id()
                                        + ".",
                                false,
                                PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER
                                        .code(),
                                PaErrorCauseEnum.ILLEGAL_ATTRIBUTE_REQUEST.id(),
                                IETFConstants.IETF_PEN_VENDORID,
                                PaAttributeTypeEnum.IETF_PA_ATTRIBUTE_REQUEST
                                        .toString(), attributes
                                        .indexOf(attributes));
                    }

                    if (ref.getType() == PaAttributeTypeEnum
                            .IETF_PA_ERROR.id()) {
                        throw new RuleException(
                                "The attribute request must not request an"
                                        + " attribute with vendor id "
                                        + IETFConstants.IETF_PEN_VENDORID
                                        + " and attribute type "
                                        + PaAttributeTypeEnum
                                            .IETF_PA_ERROR.id() + ".",
                                false,
                                PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER
                                        .code(),
                                PaErrorCauseEnum.ILLEGAL_ATTRIBUTE_REQUEST.id(),
                                IETFConstants.IETF_PEN_VENDORID,
                                PaAttributeTypeEnum.IETF_PA_ERROR.toString(),
                                attributes.indexOf(attributes));
                    }

                }
            }
        }

    }
}
