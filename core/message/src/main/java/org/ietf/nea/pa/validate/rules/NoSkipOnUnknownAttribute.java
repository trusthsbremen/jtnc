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

import java.util.Set;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that checks if no skip flag is set in the set of flags.
 *
 */
public abstract class NoSkipOnUnknownAttribute {
    /**
     * Private constructor should never be invoked.
     */
    private NoSkipOnUnknownAttribute() {
        throw new AssertionError();
    }

    /**
     * Checks if no skip flag is set in the set of flags.
     * @param flags the set of flags
     * @throws RuleException if check fails
     */
    public static void check(final Set<PaAttributeFlagEnum> flags)
            throws RuleException {
        if (flags.contains(PaAttributeFlagEnum.NOSKIP)) {
            throw new RuleException(
                    "Attribute is not supported but has No Skip set.",
                    true,
                    PaAttributeErrorCodeEnum
                        .IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code(),
                    PbErrorCauseEnum.NOT_SPECIFIED.id());
        }
    }

}
