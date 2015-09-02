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
