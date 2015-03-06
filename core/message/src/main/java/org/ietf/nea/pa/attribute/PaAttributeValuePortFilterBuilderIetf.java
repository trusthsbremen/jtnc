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
