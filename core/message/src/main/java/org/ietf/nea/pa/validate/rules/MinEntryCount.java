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

import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.ietf.nea.pa.validate.enums.PaErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
/**
 * Rule, that checks if the given list contains a minimal amount of entries.
 *
 */
public abstract class MinEntryCount {
    /**
     * Private constructor should never be invoked.
     */
    private MinEntryCount() {
        throw new AssertionError();
    }

    /**
     * Checks if the given list contains a minimal amount of entries.
     * @param minSize the minimal entry count
     * @param entries the list to check
     * @throws RuleException if check fails
     */
    public static void check(final byte minSize, final List<?> entries)
            throws RuleException {

        if (entries == null || entries.size() < minSize) {
            throw new RuleException("List has an invalid length for its type.",
                    false,
                    PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code(),
                    PaErrorCauseEnum.LENGTH_TO_SHORT.id(),
                    (entries == null) ? 0 : entries.size());
        }

    }
}
