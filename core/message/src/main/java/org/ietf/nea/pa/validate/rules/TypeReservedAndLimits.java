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

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that checks if the attribute type ID is reserved or not in range.
 *
 */
public abstract class TypeReservedAndLimits {
    /**
     * Private constructor should never be invoked.
     */
    private TypeReservedAndLimits() {
        throw new AssertionError();
    }

    /**
     * Checks if the attribute type ID is reserved or not in range.
     * @param attributeType the attribute type ID
     * @throws RuleException if check fails
     */
    public static void check(final long attributeType) throws RuleException {
        if (attributeType == IETFConstants.IETF_TYPE_RESERVED) {
            throw new RuleException("Type is set to reserved value.", false,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.TYPE_RESERVED.id(), attributeType);
        }
        if (attributeType > IETFConstants.IETF_MAX_TYPE) {
            throw new RuleException("Type is to large.", false,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.VALUE_TO_LARGE.id(), attributeType);
        }
        if (attributeType < 0) {
            throw new RuleException("Type cannot be negativ.", false,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.NEGATIV_UNSIGNED.id(), attributeType);
        }
    }
}
