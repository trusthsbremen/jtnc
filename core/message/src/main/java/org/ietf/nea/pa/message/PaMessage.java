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
package org.ietf.nea.pa.message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttribute;

import de.hsbremen.tc.tnc.message.m.message.ImMessage;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * IETF RFC 5792 integrity measurement message.
 *
 *
 */
public class PaMessage implements ImMessage {

    private final PaMessageHeader header;
    private final List<PaAttribute> attributes;

    /**
     * Creates the message with the given message header and attributes.
     * @param header the IETF RFC 5792 compliant header
     * @param attributes the IETF RFC 5792 compliant attributes
     */
    public PaMessage(final PaMessageHeader header,
            final List<PaAttribute> attributes) {
        NotNull.check("Message header cannot be null.", header);
        NotNull.check("Attributes cannot be null.", attributes);

        this.header = header;
        this.attributes = attributes;
    }

    @Override
    public PaMessageHeader getHeader() {
        return this.header;
    }

    @Override
    public List<PaAttribute> getAttributes() {
        return Collections.unmodifiableList(this.attributes);
    }

    @Override
    public String toString() {
        return "PaMessage [header=" + this.header.toString() + ", attributes="
                + Arrays.toString(this.attributes.toArray()) + "]";
    }
    
    

}
