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
package org.ietf.nea.pa.attribute;

import org.ietf.nea.pa.attribute.util.AbstractPaAttributeValueErrorInformation;

/**
 * IETF RFC 5792 integrity measurement error attribute value.
 *
 * @author Carl-Heinz Genzel
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PaAttributeValueError [errorVendorId=" + this.errorVendorId
                + ", errorCode=" + this.errorCode + ", errorInformation="
                + this.errorInformation.toString() + "]";
    }
}
