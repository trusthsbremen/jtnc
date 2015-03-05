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

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageHeaderBuilder;

/**
 * Generic builder to build a TNCCS message header compliant to RFC 5793. It can
 * be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface PbMessageHeaderBuilder extends TnccsMessageHeaderBuilder {

    /**
     * Sets the message flags encoded in one byte.
     *
     * @param flags the message flags
     * @return this builder
     */
    PbMessageHeaderBuilder setFlags(byte flags);

    /**
     * Sets the message vendor ID.
     *
     * @param vendorId the vendor ID
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbMessageHeaderBuilder setVendorId(long vendorId) throws RuleException;

    /**
     * Sets the message type.
     *
     * @param type the message type ID
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbMessageHeaderBuilder setType(long type) throws RuleException;

    /**
     * Sets the message length.
     *
     * @param length the message length
     * @return this builder
     * @throws RuleException if given value is not valid
     */
    PbMessageHeaderBuilder setLength(long length) throws RuleException;
}
