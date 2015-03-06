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
package de.hsbremen.tc.tnc.im.adapter.data;

import java.util.Arrays;

/**
 * Holds a message concerning an integrity measurement
 * component including the message header values and
 * the message as raw byte array.
 *
 *
 */
public class ImRawComponent extends AbstractImComponent {

    private final byte imFlags; // 8 bit(s)

    private final byte[] message;

    /**
     * Creates a component with the necessary address attributes and
     * the message as raw byte array.
     *
     * @param flags the message flags as composed byte
     * @param vendorId the vendor ID describing the component
     * @param type the type ID describing the component
     * @param collectorId the referred IMC
     * @param validatorId the referred IMV
     * @param message the raw message
     */
    ImRawComponent(final byte flags,
            final long vendorId, final long type,
            final long collectorId, final long validatorId,
            final byte[] message) {
        super(vendorId, type, collectorId, validatorId);

        this.imFlags = flags;
        this.message = message;
    }

    /**
     * Returns the flags value.
     * @return the flags value
     */
    public byte getImFlags() {
        return this.imFlags;
    }

    /**
     * Returns a copy of the raw message as byte array.
     * @return the raw message
     */
    public byte[] getMessage() {
        return Arrays.copyOf(message, message.length);
    }

}
