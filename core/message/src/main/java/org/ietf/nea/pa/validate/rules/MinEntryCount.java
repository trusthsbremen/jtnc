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
