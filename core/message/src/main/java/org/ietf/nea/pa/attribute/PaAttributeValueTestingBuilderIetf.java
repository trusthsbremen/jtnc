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

import java.nio.charset.Charset;

import de.hsbremen.tc.tnc.message.exception.RuleException;

/**
 * Builder to build an integrity measurement test attribute value compliant to
 * RFC 5792. It evaluates the given values and can be used in a fluent way.
 *
 *
 */
public class PaAttributeValueTestingBuilderIetf implements
        PaAttributeValueTestingBuilder {

    private long length;
    private String content;

    /**
     * Creates the builder using default values.
     * <ul>
     * <li>Length: 0</li>
     * <li>Content: ""</li>
     * </ul>
     */
    public PaAttributeValueTestingBuilderIetf() {
        this.length = 0;
        this.content = "";

    }

    @Override
    public PaAttributeValueTestingBuilder setContent(final String content)
            throws RuleException {

        if (content != null) {
            this.content = content;
            this.updateLength();
        }

        return this;
    }

    @Override
    public PaAttributeValueTesting toObject() {
        return new PaAttributeValueTesting(this.length, this.content);
    }

    @Override
    public PaAttributeValueTestingBuilder newInstance() {
        return new PaAttributeValueTestingBuilderIetf();
    }

    /**
     * Updates the length according to the current content.
     */
    private void updateLength() {
        this.length = 0;
        if (content.length() > 0) {
            this.length += content.getBytes(Charset.forName("UTF-8")).length;
        }
    }

}
