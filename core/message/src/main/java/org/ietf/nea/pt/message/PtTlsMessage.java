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

import org.ietf.nea.pt.value.PtTlsMessageValue;

import de.hsbremen.tc.tnc.message.t.message.TransportMessage;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * IETF RFC 6876 transport message.
 *
 *
 */
public class PtTlsMessage implements TransportMessage {

    private final PtTlsMessageHeader header;
    private final PtTlsMessageValue value;

    /**
     * Creates the message with the given message header and attributes.
     *
     * @param header the IETF RFC 6876compliant header
     * @param value the IETF RFC 6876 compliant value
     */
    public PtTlsMessage(final PtTlsMessageHeader header,
            final PtTlsMessageValue value) {
        NotNull.check("Message header cannot be null.", header);
        NotNull.check("Value cannot be null.", value);
        this.header = header;
        this.value = value;
    }

    @Override
    public PtTlsMessageHeader getHeader() {
        return this.header;
    }

    @Override
    public PtTlsMessageValue getValue() {
        return this.value;
    }

}
