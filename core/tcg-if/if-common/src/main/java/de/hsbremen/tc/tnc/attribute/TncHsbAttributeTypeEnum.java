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
package de.hsbremen.tc.tnc.attribute;

/**
 * Enumeration of additional attributes used by this implementation.
 *
 *
 */
public enum TncHsbAttributeTypeEnum implements TncAttributeType {

    /**
     * Transport connection ID provided by the underlying connection. (type
     * String, may get from a IM(C/V)Connection) Has the value 254 because the
     * lower numbers may be used by more important attributes.
     */
    HSB_ATTRIBUTEID_IFT_ID(0x0025B1FEL),
    /**
     * Current round trips provided by the TNCC/S session for other components
     * and IMC/V (type long, may get from a IM(C/V)Connection) Has the value 255
     * because the lower numbers may be used by more important attributes.
     */
    HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS(0x0025B1FFL);

    private long id;

    /**
     * Creates a attribute type enumeration value with an ID.
     *
     * @param id the type ID
     */
    private TncHsbAttributeTypeEnum(final long id) {
        this.id = id;
    }

    @Override
    public long id() {
        return this.id;
    }
}
