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

import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pa.attribute.PaAttributeValue;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.util.NotNull;
/**
 * Rule, that checks if an attribute needs a no skip flag and the no skip
 * flag is contained in the set of lags.
 *
 */
public abstract class PaAttributeNoSkip {
    /**
     * Private constructor should never be invoked.
     */
    private PaAttributeNoSkip() {
        throw new AssertionError();
    }

    /**
     * Checks if an attribute needs a no skip flag and the no skip
     * flag is contained in the set of lags.
     * @param value the attribute
     * @param flags the set of flags
     * @throws RuleException if check fails
     */
    public static void check(final PaAttributeValue value,
            final Set<PaAttributeFlagEnum> flags) throws RuleException {
        NotNull.check("Flags can not be null.", flags);

        NotNull.check("Value can not be null.", value);

        if (!value.isOmittable()) {
            if (flags.isEmpty()
                    || !flags.contains(PaAttributeFlagEnum.NOSKIP)) {
                throw new RuleException(
                        "NOSKIP must be set for this attribute.", true,
                        PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                        PaErrorCauseEnum.NOSKIP_MISSING.id(),
                        Arrays.toString(flags.toArray()));
            }
        } else {
            if (!flags.isEmpty()
                    && flags.contains(PaAttributeFlagEnum.NOSKIP)) {
                throw new RuleException(
                        "NOSKIP not allowed in this attribute.", true,
                        PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                        PaErrorCauseEnum.NOSKIP_NOT_ALLOWED.id(),
                        Arrays.toString(flags.toArray()));
            }
        }
    }
}
