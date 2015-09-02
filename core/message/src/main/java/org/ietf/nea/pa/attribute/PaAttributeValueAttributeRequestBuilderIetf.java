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
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.attribute.util.AttributeReferenceEntry;
import org.ietf.nea.pa.validate.rules.MinEntryCount;
import org.ietf.nea.pa.validate.rules.RequestingForbiddenAttributes;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement attribute request attribute value
 * compliant to RFC 5792. It evaluates the given values and can be used in a
 * fluent way.
 *
 *
 */
public class PaAttributeValueAttributeRequestBuilderIetf implements
        PaAttributeValueAttributeRequestBuilder {

    private long length;
    private List<AttributeReferenceEntry> references;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>References: Empty list</li>
     * </ul>
     */
    public PaAttributeValueAttributeRequestBuilderIetf() {
        this.length = 0;
        this.references = new LinkedList<>();
    }

    @Override
    public PaAttributeValueAttributeRequestBuilder addReferences(
            final AttributeReferenceEntry reference,
            final AttributeReferenceEntry... references) throws RuleException {

        List<AttributeReferenceEntry> temp = new ArrayList<>();

        if (reference != null) {
            temp.add(reference);
        }

        if (references != null) {
            for (AttributeReferenceEntry attributeReference : references) {
                if (attributeReference != null) {
                    temp.add(attributeReference);
                }
            }
        }

        RequestingForbiddenAttributes.check(temp);

        this.references.addAll(temp);
        this.length = (PaAttributeTlvFixedLengthEnum.ATT_REQ.length()
                * this.references.size());

        MinEntryCount.check((byte) 1, this.references);

        return this;
    }

    @Override
    public PaAttributeValueAttributeRequest toObject() {

        return new PaAttributeValueAttributeRequest(length, references);
    }

    @Override
    public PaAttributeValueAttributeRequestBuilder newInstance() {

        return new PaAttributeValueAttributeRequestBuilderIetf();
    }

}
