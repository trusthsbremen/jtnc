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
 * Enumeration with additional attributes used by this implementation.
 *
 * @author Carl-Heinz Genzel
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
