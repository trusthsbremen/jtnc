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
 * Generic builder to build a transport version request message value compliant
 * to RFC 6876. It can be used in a fluent way.
 *
 *
 */
public interface PtTlsMessageValueVersionRequestBuilder extends
        TransportMessageValueBuilder {

    /**
     * Sets the preferred transport message version.
     *
     * @param version the preferred version
     * @return the builder
     * @throws RuleException if given value is not valid
     */
    PtTlsMessageValueVersionRequestBuilder setPreferredVersion(short version)
            throws RuleException;

    /**
     * Sets the maximum transport message version.
     *
     * @param version the maximum version
     * @return the builder
     * @throws RuleException if given value is not valid
     */
    PtTlsMessageValueVersionRequestBuilder setMaxVersion(short version)
            throws RuleException;

    /**
     * Sets the minimum transport message version.
     *
     * @param version the minimum version
     * @return the builder
     * @throws RuleException if given value is not valid
     */
    PtTlsMessageValueVersionRequestBuilder setMinVersion(short version)
            throws RuleException;
}
