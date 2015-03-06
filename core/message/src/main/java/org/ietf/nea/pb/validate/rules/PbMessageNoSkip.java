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
package org.ietf.nea.pb.validate.rules;

import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValue;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.util.NotNull;
/**
 * Rule, that checks if a message needs a no skip flag and the no skip.
 *
 */
public abstract class PbMessageNoSkip {
    /**
     * Private constructor should never be invoked.
     */
    private PbMessageNoSkip() {
        throw new AssertionError();
    }
    /**
     * Checks if a message needs a no skip flag and the no skip.
     * flag is contained in the set of lags.
     * @param value the message
     * @param flags the set of flags
     * @throws RuleException if check fails
     */
    public static void check(final PbMessageValue value,
            final Set<PbMessageFlagEnum> flags) throws RuleException {
        NotNull.check("Flags can not be null.", flags);

        NotNull.check("Value can not be null.", value);

        if (!value.isOmittable()) {
            if (flags.isEmpty() || !flags.contains(PbMessageFlagEnum.NOSKIP)) {
                throw new RuleException("NOSKIP must be set for this message.",
                        true,
                        PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                        PbErrorCauseEnum.NOSKIP_MISSING.id(),
                        Arrays.toString(flags.toArray()));
            }
        } else {
            if (!flags.isEmpty() && flags.contains(PbMessageFlagEnum.NOSKIP)) {
                throw new RuleException("NOSKIP not allowed in this message.",
                        true,
                        PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                        PbErrorCauseEnum.NOSKIP_NOT_ALLOWED.id(),
                        Arrays.toString(flags.toArray()));
            }
        }
    }
}
