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
package org.ietf.nea.pb.batch;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatchHeaderBuilder;

/**
 * Generic builder to build a TNCCS batch header compliant to RFC 5793. It can
 * be used in a fluent way.
 *
 *
 */
public interface PbBatchHeaderBuilder extends TnccsBatchHeaderBuilder {

    /**
     * Sets the batch format version.
     *
     * @param version the batch version
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbBatchHeaderBuilder setVersion(short version) throws RuleException;

    /**
     * Sets the batch sending direction.
     *
     * @param direction the direction (e.g. to server or client)
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbBatchHeaderBuilder setDirection(byte direction) throws RuleException;

    /**
     * Sets the batch type.
     *
     * @param type the batch type (e.g. server data, result, ...)
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbBatchHeaderBuilder setType(byte type) throws RuleException;

    /**
     * Sets the batch length.
     *
     * @param length the batch length
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbBatchHeaderBuilder setLength(long length) throws RuleException;

}
