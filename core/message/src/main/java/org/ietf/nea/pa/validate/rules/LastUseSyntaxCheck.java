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

import java.util.regex.Pattern;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that Checks if the syntax of the date for an operation's
 * last us is valid.
 *
 */
public abstract class LastUseSyntaxCheck {
    /**
     * Private constructor should never be invoked.
     */
    private LastUseSyntaxCheck() {
        throw new AssertionError();
    }

    /**
     * Checks if the syntax of the date for an operation's last us is valid.
     * @param value the date value
     * @throws RuleException if check fails
     */
    public static void check(final String value) throws RuleException {
        if (value != null) {
            if (!Pattern.matches(
                    "\\d{4}\\-\\d{2}\\-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z", value)) {
                throw new RuleException(
                        "Attribute contains an unsupported time format: "
                                + value + ".", false,
                        PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                        PaErrorCauseEnum.TIME_FORMAT_NOT_VALID.id(), value);
            }
        }
    }
}
