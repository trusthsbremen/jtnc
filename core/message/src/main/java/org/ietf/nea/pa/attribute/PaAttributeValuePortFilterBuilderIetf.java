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
package org.ietf.nea.pa.attribute;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;
import org.ietf.nea.pa.validate.rules.MinEntryCount;
import org.ietf.nea.pa.validate.rules.OpposingPortFilterEntries;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement port filter attribute value
 * compliant to RFC 5792. It evaluates the given values and can be used in a
 * fluent way.
 *
 *
 */
public class PaAttributeValuePortFilterBuilderIetf implements
        PaAttributeValuePortFilterBuilder {

    private long length;
    private List<PortFilterEntry> filterEntries;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>Entries: Empty list</li>
     * </ul>
     */
    public PaAttributeValuePortFilterBuilderIetf() {
        this.length = 0;
        this.filterEntries = new LinkedList<>();
    }

    @Override
    public PaAttributeValuePortFilterBuilder addEntries(
            final PortFilterEntry entry,
            final PortFilterEntry... entries) throws RuleException {

        List<PortFilterEntry> temp = new ArrayList<>();

        if (entry != null) {
            temp.add(entry);
        }

        if (entries != null) {
            for (PortFilterEntry pEntry : entries) {
                if (pEntry != null) {
                    temp.add(pEntry);
                }
            }
        }

        OpposingPortFilterEntries.check(temp);

        this.filterEntries.addAll(temp);
        this.length = (PaAttributeTlvFixedLengthEnum.PORT_FT.length()
                * this.filterEntries.size());

        MinEntryCount.check((byte) 1, this.filterEntries);

        return this;
    }

    @Override
    public PaAttributeValuePortFilter toObject() {

        return new PaAttributeValuePortFilter(this.length, this.filterEntries);
    }

    @Override
    public PaAttributeValuePortFilterBuilder newInstance() {
        return new PaAttributeValuePortFilterBuilderIetf();
    }

}
