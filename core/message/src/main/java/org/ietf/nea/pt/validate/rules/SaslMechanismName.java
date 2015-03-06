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
package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.util.NotNull;
/**
 * Rule, that checks if SASL mechanism name meets the
 * RFC 4422 conventions.
 *
 */
public abstract class SaslMechanismName {
    /**
     * Private constructor should never be invoked.
     */
    private SaslMechanismName() {
        throw new AssertionError();
    }

    /**
     * Checks if SASL mechanism name meets the RFC 4422 conventions.
     * @param value the mechanism name
     * @throws RuleException if check fails
     */
    public static void check(final String value) throws RuleException {
        NotNull.check("Value cannot be null.", value);
        if (value.contains("\0")) {
            throw new RuleException(
                    "Message contains Null termination sequences.", true,
                    PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PtTlsErrorCauseEnum.NULL_TERMINATION.id(), value);
        }

        if (!value.matches("[A-Z0-9\\-_]{1,20}")) {
            throw new RuleException(
                    "SASL mechanism name does not match RFC 4422 conventions.",
                    true,
                    PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PtTlsErrorCauseEnum.SASL_NAMING_MISMATCH.id(), value);
        }
    }
}
