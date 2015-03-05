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
package org.ietf.nea.pt.value;

import java.util.Arrays;

/**
 * IETF RFC 6876 transport error message value.
 *
 * @author Carl-Heinz Genzel
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
