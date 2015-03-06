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
