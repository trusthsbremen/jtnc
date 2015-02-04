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
 * Enumeration with known attributes server-only attributes.
 *
 * @author Carl-Heinz Genzel
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
