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
