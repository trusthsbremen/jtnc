/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.ietf.nea.pa.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ietf.nea.pa.attribute.util.PortFilterEntry;

/**
 * IETF RFC 5792 integrity measurement port filter attribute value.
 *
 *
 */
public class PaAttributeValuePortFilter extends AbstractPaAttributeValue {

    private final List<PortFilterEntry> filterEntries; // 32 bit(s) value must
                                                       // have at minimum one
                                                       // entry

    /**
     * Creates the attribute value with the given values.
     * @param length the value length
     * @param filterEntries the list of port filter entries
     */
    PaAttributeValuePortFilter(final long length,
            final List<PortFilterEntry> filterEntries) {
        super(length);

        this.filterEntries = (filterEntries != null)
                ? filterEntries : new ArrayList<PortFilterEntry>(0);
    }

    /**
     * Returns the list of port filter entries.
     * @return the port filter entry list
     */
    public List<PortFilterEntry> getFilterEntries() {
        return Collections.unmodifiableList(this.filterEntries);
    }

    @Override
    public String toString() {
        return "PaAttributeValuePortFilter [filterEntries="
                + Arrays.toString(
                        this.filterEntries.toArray(new PortFilterEntry[0]))
                + "]";
    }
}
