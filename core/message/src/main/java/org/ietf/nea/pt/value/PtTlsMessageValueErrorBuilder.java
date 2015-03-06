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

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

/**
 * Generic builder to build a transport error message value compliant to RFC
 * 6876. It can be used in a fluent way.
 *
 *
 */
public interface PtTlsMessageValueErrorBuilder extends
        TransportMessageValueBuilder {

    /**
     * Sets the error vendor ID.
     *
     * @param errorVendorId the error vendor ID
     * @return the builder
     * @throws RuleException if given value is not valid
     */
    PtTlsMessageValueErrorBuilder setErrorVendorId(long errorVendorId)
            throws RuleException;

    /**
     * Sets the code describing the error.
     *
     * @param errorCode the error code
     * @return the builder
     * @throws RuleException if given value is not valid
     */
    PtTlsMessageValueErrorBuilder setErrorCode(long errorCode)
            throws RuleException;

    /**
     * Sets the partial copy of the erroneous message.
     *
     * @param message the partial byte copy
     * @return the builder
     */
    PtTlsMessageValueErrorBuilder setPartialMessage(byte[] message);

}
