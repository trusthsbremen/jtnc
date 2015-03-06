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
