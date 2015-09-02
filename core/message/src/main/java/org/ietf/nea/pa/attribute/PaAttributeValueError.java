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
package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueErrorInformation;

/**
 * IETF RFC 5792 integrity measurement error attribute value.
 *
 *
 */
public class PaAttributeValueError extends AbstractPaAttributeValue {

    private final long errorVendorId; // 24 bit(s)
    private final long errorCode; // 32 bit(s)

    private final AbstractPaAttributeValueErrorInformation errorInformation;

    /**
     * Creates the attribute value with the given values.
     * @param length the value length
     * @param errorVendorId the error vendor ID
     * @param errorCode the error code describing the error
     * @param errorInformation the additional error information
     */
    PaAttributeValueError(final long length, final long errorVendorId,
            final long errorCode,
            final AbstractPaAttributeValueErrorInformation errorInformation) {
        super(length);
        this.errorVendorId = errorVendorId;
        this.errorCode = errorCode;
        this.errorInformation = errorInformation;
    }

    /**
     * Returns the error vendor ID.
     * @return the vendor ID
     */
    public long getErrorVendorId() {
        return this.errorVendorId;
    }

    /**
     * Returns the error code.
     * @return the error code
     */
    public long getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns the additional error information.
     * @return the error information
     */
    public AbstractPaAttributeValueErrorInformation getErrorInformation() {
        return this.errorInformation;
    }

    @Override
    public String toString() {
        return "PaAttributeValueError [errorVendorId=" + this.errorVendorId
                + ", errorCode=" + this.errorCode + ", errorInformation="
                + this.errorInformation.toString() + "]";
    }
}
