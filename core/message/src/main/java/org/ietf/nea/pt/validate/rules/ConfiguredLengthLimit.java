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

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Rule, that checks if a given length does not exceed a configured maximum
 * length.
 *
 */
public abstract class ConfiguredLengthLimit {
    /**
     * Private constructor should never be invoked.
     */
    private ConfiguredLengthLimit() {
        throw new AssertionError();
    }

    /**
     * Checks if a given length does not exceed a configured maximum
     * length.
     * @param length the actual length
     * @param maxlength the maximum length
     * @throws RuleException if check fails
     */
    public static void check(final long length, final long maxlength)
            throws RuleException {

        if (maxlength != HSBConstants.HSB_TRSPT_MAX_MESSAGE_SIZE_UNKNOWN
                && maxlength != HSBConstants
                    .HSB_TRSPT_MAX_MESSAGE_SIZE_UNLIMITED) {
            if (length > maxlength) {
                throw new RuleException(
                        "Length is to large. Configured limit is " + maxlength
                                + ".",
                        true,
                        PtTlsMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                        PtTlsErrorCauseEnum.VALUE_TO_LARGE.id(), length,
                        maxlength);

            }
        }
    }
}
