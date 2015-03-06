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
package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.util.AbstractPbMessageValueRemediationParameter;

/**
 * IETF RFC 5793 TNCCS remediation parameters message value.
 *
 *
 */
public class PbMessageValueRemediationParameters extends
    AbstractPbMessageValue {

    private final long rpVendorId; // 24 bit(s)
    private final long rpType; // 32 bit(s)

    private final AbstractPbMessageValueRemediationParameter parameter;

    /**
     * Creates the message value with the given values.
     *
     * @param length the value length
     * @param rpVendorId the remediation vendor ID
     * @param rpType the type describing the remediation
     * @param parameter the additional remediation parameter
     */
    PbMessageValueRemediationParameters(final long length,
            final long rpVendorId, final long rpType,
            final AbstractPbMessageValueRemediationParameter parameter) {
        super(length);
        this.rpVendorId = rpVendorId;
        this.rpType = rpType;
        this.parameter = parameter;
    }

    /**
     * Returns the remediation vendor ID.
     * @return the vendor ID
     */
    public long getRpVendorId() {
        return this.rpVendorId;
    }

    /**
     * Returns the remediation type ID.
     * @return the remediation type ID
     */
    public long getRpType() {
        return this.rpType;
    }

    /**
     * Returns the remediation parameter.
     * @return the parameter
     */
    public AbstractPbMessageValueRemediationParameter getParameter() {
        return this.parameter;
    }

    @Override
    public String toString() {
        return "PbMessageValueRemediationParameters [rpVendorId="
                + this.rpVendorId + ", rpType=" + this.rpType + ", parameter="
                + this.parameter.toString() + "]";
    }
}
