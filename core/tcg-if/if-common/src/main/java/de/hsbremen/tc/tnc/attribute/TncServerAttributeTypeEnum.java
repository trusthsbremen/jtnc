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
 * Enumeration of known attributes server-only attributes.
 *
 *
 */
public enum TncServerAttributeTypeEnum implements TncAttributeType {

    /**
     * Reason for IMV Recommendation (type String, may set for an
     * IMVConnection).
     */
    TNC_ATTRIBUTEID_REASON_STRING(2L),

    /**
     * Language(s) for Reason String as an RFC 3066 language tag (type String,
     * may set for an IMVConnection).
     */
    TNC_ATTRIBUTEID_REASON_LANGUAGE(3L),

    /**
     * Contents of SOH (type byte [], may get from an IMVConnection).
     */
    TNC_ATTRIBUTEID_SOH(0x00559706L),

    /**
     * Contents of SSOH (type byte [], may get from an IMVConnection).
     */
    TNC_ATTRIBUTEID_SSOH(0x00559707L),

    /**
     * Primary ID for IMV (type Long, may get from a TNCS or an IMVConnection).
     */
    TNC_ATTRIBUTEID_PRIMARY_IMV_ID(0x00559710L);

    private long id;

    /**
     * Creates a attribute type enumeration value with an ID.
     *
     * @param id the type ID
     */
    private TncServerAttributeTypeEnum(final long id) {
        this.id = id;
    }

    @Override
    public long id() {
        return this.id;
    }
}
