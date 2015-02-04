/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Carl-Heinz Genzel
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
package de.hsbremen.tc.tnc;

/**
 * Holds several constants, defined by the IETF.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class IETFConstants {

    /**
     * Private constructor should never be invoked.
     */
    private IETFConstants() {
        throw new AssertionError();
    }

    /**
     * IANA PEN for the standard TNC objects defined by the IETF.
     */
    public static final long IETF_PEN_VENDORID = 0L;

    /**
     * Reserved value for message type.
     */
    public static final long IETF_TYPE_RESERVED = 0xFFFFFFFFL;

    /**
     * Reserved vendor ID for message.
     */
    public static final long IETF_VENDOR_ID_RESERVED = 0xFFFFFFL;

    /**
     * Maximum message length.
     */
    public static final long IETF_MAX_LENGTH = 0xFFFFFFFFL;

    /**
     * Maximum vendor ID.
     */
    public static final long IETF_MAX_VENDOR_ID = 0xFFFFFFL;

    /**
     * Maximum message type.
     */
    public static final long IETF_MAX_TYPE = 0xFFFFFFFFL;

    /**
     * Maximum error code.
     */
    public static final int IETF_MAX_ERROR_CODE = 0xFFFF;

    /**
     * Maximum language code length.
     */
    public static final short IETF_MAX_LANG_CODE_LENGTH = 0xFF;

    /**
     * Binding version number for the binding in RFC5793.
     */
    public static final byte IETF_RFC5793_VERSION_NUMBER = 2;

    /**
     * Binding version number for the binding in RFC6876.
     */
    public static final byte IETF_RFC6876_VERSION_NUMBER = 1;

}
