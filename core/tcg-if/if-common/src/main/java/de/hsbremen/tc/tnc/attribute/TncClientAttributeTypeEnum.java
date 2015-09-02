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
package de.hsbremen.tc.tnc.attribute;

/**
 * Enumeration of known client-only attributes.
 *
 *
 */
public enum TncClientAttributeTypeEnum implements TncAttributeType {

    /**
     * Contents of SOHR (type byte [], may get from an IMCConnection).
     */
    TNC_ATTRIBUTEID_SOHR(0x00559708L),

    /**
     * Contents of SSOHR (type byte [], may get from an IMCConnection).
     */
    TNC_ATTRIBUTEID_SSOHR(0x00559709L),

    /**
     * Flag to indicate if IMC supports TNCS sending first message (type
     * boolean, may get from a IMC).
     */
    TNC_ATTRIBUTEID_IMC_SPTS_TNCS1(0x0055970FL),

    /**
     * IMC identifier assigned by the TNCC when the TNCC loaded this IMC.
     */
    TNC_ATTRIBUTEID_PRIMARY_IMC_ID(0x00559711L);

    private long id;

    /**
     * Creates a attribute type enumeration value with an ID.
     * @param id the type ID
     */
    private TncClientAttributeTypeEnum(final long id) {
        this.id = id;
    }

    @Override
    public long id() {
        return this.id;
    }

}
