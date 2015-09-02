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
package de.hsbremen.tc.tnc;

/**
 * Holds several constants, defined by the IETF.
 *
 *
 */
public final class IETFConstants {

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
     * Binding version number for the binding in RFC 5793.
     */
    public static final byte IETF_RFC5793_VERSION_NUMBER = 2;

    /**
     * Binding version number for the binding in RFC 5792.
     */
    public static final byte IETF_RFC5792_VERSION_NUMBER = 1;
    
    /**
     * Binding version number for the binding in RFC 6876.
     */
    public static final byte IETF_RFC6876_VERSION_NUMBER = 1;

}
