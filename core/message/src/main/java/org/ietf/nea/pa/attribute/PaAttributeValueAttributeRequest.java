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

import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;

/**
 * IETF RFC 5792 integrity measurement attribute request attribute value.
 *
 *
 */
public class PaAttributeValueAttributeRequest extends AbstractPaAttributeValue {

    private final List<AttributeReferenceEntry> references; // 24 bit(s) key, 32
                                                       // bit(s) value must have
                                                       // at minimum one entry

    /**
     * Creates the attribute value with the given values.
     * @param length the value length
     * @param references the reference for the requested attributes
     */
    PaAttributeValueAttributeRequest(final long length,
            final List<AttributeReferenceEntry> references) {
        super(length);

        this.references = (references != null)
                ? references : new ArrayList<AttributeReferenceEntry>(0);
    }

    /**
     * Returns the list of references for the requested attributes.
     * @return the list of references
     */
    public List<AttributeReferenceEntry> getReferences() {
        return Collections.unmodifiableList(this.references);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PaAttributeValueAttributeRequest [references="
                + Arrays.toString(
                        this.references.toArray(new AttributeReferenceEntry[0]))
                + "]";
    }
}
