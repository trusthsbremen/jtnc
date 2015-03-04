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
package org.ietf.nea.pa.message;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.message.ImMessageHeaderBuilder;

/**
 * Generic builder to build an integrity measurement component message header
 * compliant to RFC 5792. It can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface PaMessageHeaderBuilder extends ImMessageHeaderBuilder {

    /**
     * Sets the message format version.
     *
     * @param version the message version.
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PaMessageHeaderBuilder setVersion(short version) throws RuleException;

    /**
     * Sets the message identifier.
     *
     * @param identifier the message identifier
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PaMessageHeaderBuilder setIdentifier(long identifier) throws RuleException;

    /**
     * Sets the message length.
     * @param length the message length
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PaMessageHeaderBuilder setLength(long length) throws RuleException;
}
