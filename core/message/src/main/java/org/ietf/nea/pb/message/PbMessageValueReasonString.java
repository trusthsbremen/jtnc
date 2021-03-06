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
package org.ietf.nea.pb.message;

import java.nio.charset.Charset;

/**
 * IETF RFC 5793 TNCCS reason string message value.
 *
 *
 */
public class PbMessageValueReasonString extends AbstractPbMessageValue {

    public static final byte FIXED_LENGTH = 5;

    private final long stringLength; // 32 bit(s) length of the string in octets
    private final String reasonString; // variable length, UTF-8 encoded, NUL
                                       // termination MUST NOT be included.
    private final short langCodeLength; // 8 bit(s) length of language code in
                                        // octets, 0 = language unknown
    private final String langCode; // variable length, US-ASCII string composed
                                   // of a well-formed RFC 4646 [3] language tag

    /**
     * Creates the message value with the given values.
     *
     * @param length the value length
     * @param reasonString the reason string
     * @param langCode the RFC 4646 language identifier
     */
    PbMessageValueReasonString(final long length, final String reasonString,
            final String langCode) {
        super(length);
        this.reasonString = reasonString;
        this.stringLength = reasonString.getBytes(
                Charset.forName("UTF-8")).length;
        this.langCode = langCode;
        this.langCodeLength = (byte) langCode.getBytes(
                Charset.forName("US-ASCII")).length;
    }

    /**
     * Returns the reason string length.
     * @return the reason string length
     */
    public long getStringLength() {
        return this.stringLength;
    }

    /**
     * Returns the language code length.
     * @return the language code length
     */
    public short getLangCodeLength() {
        return this.langCodeLength;
    }

    /**
     * Returns the reason string.
     * @return the reason string
     */
    public String getReasonString() {
        return this.reasonString;
    }

    /**
     * Returns the RFC 4646 language code.
     * @return the language code
     */
    public String getLangCode() {
        return this.langCode;
    }

    @Override
    public String toString() {
        return "PbMessageValueReasonString [stringLength=" + this.stringLength
                + ", reasonString=" + this.reasonString + ", langCodeLength="
                + this.langCodeLength + ", langCode=" + this.langCode + "]";
    }

}
