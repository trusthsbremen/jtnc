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
package org.ietf.nea.pt.message;

import de.hsbremen.tc.tnc.message.t.message.TransportMessageHeader;

/**
 * IETF RFC 6876 transport message header.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class PtTlsMessageHeader implements TransportMessageHeader {

    private final long vendorId; // 24 bit(s)
    private final long type; // 32 bit(s)
    private final long length; // 32 bit(s) min value is 16 for the 16 bytes in
                               // this header
    private final long identifier; // 32 bit(s)

    /**
     * Creates the header with the given values.
     * @param vendorId the vendor ID
     * @param type the message type
     * @param length the message length
     * @param identifier the message identifier
     */
    public PtTlsMessageHeader(final long vendorId, final long type,
            final long length, final long identifier) {
        this.vendorId = vendorId;
        this.type = type;
        this.length = length;
        this.identifier = identifier;
    }

    /**
     * @return the vendorId
     */
    public long getVendorId() {
        return vendorId;
    }

    /**
     * @return the type
     */
    public long getMessageType() {
        return type;
    }

    /**
     * @return the length
     */
    @Override
    public long getLength() {
        return length;
    }

    /**
     * @return the identifier
     */
    @Override
    public long getIdentifier() {
        return this.identifier;
    }

}
