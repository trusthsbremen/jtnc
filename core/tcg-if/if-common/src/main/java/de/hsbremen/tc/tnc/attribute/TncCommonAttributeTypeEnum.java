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
 * Enumeration of known attributes used by client and server.
 *
 *
 */
public enum TncCommonAttributeTypeEnum implements TncAttributeType {

    /**
     * Preferred human-readable language for a TNCS (type String, may get from a
     * IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_PREFERRED_LANGUAGE(1L),

    /**
     * Maximum round trips supported by the underlying protocol (type Integer,
     * may get from an IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_MAX_ROUND_TRIPS(0x00559700L),

    /**
     * Maximum message size supported by the underlying protocol (type Integer,
     * may get from an IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE(0x00559701L),

    /**
     * Diffie-Hellman Pre-Negotiation value provided by the underlying protocol
     * (type byte[], may get from an IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_DHPN_VALUE(0x00559702L),

    /**
     * Flag indicating if the connection supports long message types (type
     * boolean, may get from an IM(C/V)Connection).
     */
    // FIXME this is probably never used, because instanceof
    // IM(C/V)ConnectionLong should be used.
    // Genzel 2014-09-08
    TNC_ATTRIBUTEID_HAS_LONG_TYPES(0x00559703L),

    /**
     * Flag indicating if the connection supports exclusive delivery of messages
     * (type boolean, may get from an IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_HAS_EXCLUSIVE(0x00559704L),

    /**
     * Flag indicating if the connection supports SOH functions (type boolean,
     * may get from an IM(C/V)Connection).
     */
    // FIXME this is probably never used, because instanceof
    // IM(C/V)ConnectionSOH should be used.
    // Genzel 2014-11-08
    TNC_ATTRIBUTEID_HAS_SOH(0x00559705L),

    /**
     * IF-TNCCS Protocol Name (type String, may get from an IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_IFTNCCS_PROTOCOL(0x0055970AL),

    /**
     * IF-TNCCS Protocol Version (type String, may get from an
     * IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_IFTNCCS_VERSION(0x0055970BL),

    /**
     * IF-T Protocol Name (type String, may get from an IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_IFT_PROTOCOL(0x0055970CL),

    /**
     * IF-T Protocol Version (type String, may get from an IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_IFT_VERSION(0x0055970DL),

    /**
     * TLS-Unique value provided by the underlying protocol (type byte[], may
     * get from a IM(C/V)Connection).
     */
    TNC_ATTRIBUTEID_TLS_UNIQUE(0x0055970EL);

    private long id;

    /**
     * Creates a attribute type enumeration value with an ID.
     * @param id the type ID
     */
    private TncCommonAttributeTypeEnum(final long id) {
        this.id = id;
    }

    @Override
    public long id() {
        return this.id;
    }
}
