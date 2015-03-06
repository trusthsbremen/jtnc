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
