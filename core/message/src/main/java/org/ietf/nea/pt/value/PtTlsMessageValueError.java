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
package org.ietf.nea.pt.value;

import java.util.Arrays;

/**
 * IETF RFC 6876 transport error message value.
 *
 *
 */
public class PtTlsMessageValueError extends AbstractPtTlsMessageValue {

    private final long errorVendorId; // 24 bit(s)
    private final long errorCode; // 32 bit(s)
    private final byte[] partialMessageCopy; // max 1024 byte(s)

    /**
     * Creates the message value with the given values.
     *
     * @param errorVendorId the error vendor ID
     * @param errorCode the code describing the error
     * @param length the value length
     * @param partialMessageCopy the partial copy of the message
     */
    PtTlsMessageValueError(final long errorVendorId, final long errorCode,
            final long length, final byte[] partialMessageCopy) {
        super(length);

        this.errorVendorId = errorVendorId;
        this.errorCode = errorCode;
        this.partialMessageCopy = (partialMessageCopy != null)
                ? partialMessageCopy : new byte[0];
    }

    /**
     * Returns the error vendor ID.
     *
     * @return the error vendor ID
     */
    public long getErrorVendorId() {
        return this.errorVendorId;
    }

    /**
     * Returns the error code.
     *
     * @return the error code
     */
    public long getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns the partial message copy.
     *
     * @return the message copy
     */
    public byte[] getPartialMessageCopy() {
        return Arrays.copyOf(this.partialMessageCopy,
                this.partialMessageCopy.length);
    }

}
