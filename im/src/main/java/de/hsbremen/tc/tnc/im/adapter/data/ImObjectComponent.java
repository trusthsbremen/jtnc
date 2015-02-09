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
package de.hsbremen.tc.tnc.im.adapter.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import de.hsbremen.tc.tnc.im.adapter.data.enums.PaComponentFlagsEnum;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttribute;

/**
 * Holds a message concerning an integrity measurement
 * component including the message header values and
 * the integrity measurement attributes as parsed objects.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class ImObjectComponent extends AbstractImComponent {

    private final EnumSet<PaComponentFlagsEnum> imFlags;
    private final List<? extends ImAttribute> attributes;

    /**
     * Creates a component with the necessary address attributes and
     * the integrity measurement attributes as parsed objects.
     *
     * @param flags the message flags
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param collectorId the referred IMC
     * @param validatorId the referred IMV
     * @param attributes the measurement attributes
     */
    ImObjectComponent(final PaComponentFlagsEnum[] flags,
            final long vendorId, final long type,
            final long collectorId, final long validatorId,
            final List<? extends ImAttribute> attributes) {
        super(vendorId, type, collectorId, validatorId);

        if (flags != null && flags.length > 0) {
            this.imFlags = EnumSet.copyOf(Arrays.asList(flags));
        } else {
            this.imFlags = EnumSet.noneOf(PaComponentFlagsEnum.class);
        }

        this.attributes = (attributes != null) ? attributes
                : new ArrayList<ImAttribute>(0);
    }

    /**
     * Returns the unmodifiable set of message flags.
     * @return the message flags
     */
    public Set<PaComponentFlagsEnum> getImFlags() {
        return Collections.unmodifiableSet(imFlags);
    }

    /**
     * Returns the unmodifiable list of measurement attributes.
     * @return the list of attributes
     */
    public List<? extends ImAttribute> getAttributes() {
        return Collections.unmodifiableList(this.attributes);
    }

}
