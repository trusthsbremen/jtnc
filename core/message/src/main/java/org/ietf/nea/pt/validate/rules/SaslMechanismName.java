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
package org.ietf.nea.pt.validate.rules;

import org.ietf.nea.pt.validate.enums.PtTlsErrorCauseEnum;
import org.ietf.nea.pt.value.enums.PtTlsMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.util.NotNull;
/**
 * Rule, that checks if SASL mechanism name meets the
 * RFC 4422 conventions.
 * @author Carl-Heinz Genzel
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
