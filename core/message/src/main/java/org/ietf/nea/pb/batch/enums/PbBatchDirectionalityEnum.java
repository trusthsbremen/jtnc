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
package org.ietf.nea.pb.batch.enums;

/**
 * Enumeration of known batch directions.
 *
 *
 */
public enum PbBatchDirectionalityEnum {
    /**
     * Batch directed to client.
     */
    TO_PBC(false),
    /**
     * Batch directed to server.
     */
    TO_PBS(true);

    private final boolean directionality;

    /**
     * Creates the direction with the given directionality.
     * True -> to server, false -> to client.
     *
     * @param directionality the directionality
     */
    private PbBatchDirectionalityEnum(final boolean directionality) {
        this.directionality = directionality;
    }

    /**
     * Returns the directionality as boolean.
     * @return the directionality
     */
    public boolean directionality() {
        return this.directionality;
    }

    /**
     * Returns the directionality as bit value (e.g. 0x0 or 0x1).
     * @return the directionality bit
     */
    public byte toDirectionalityBit() {
        return (byte) (this.directionality ? 1 : 0);
    }

    /**
     * Return the directionality for the given bit value.
     * @param directionality the directionality bit
     * @return a direction or null
     */
    public static PbBatchDirectionalityEnum fromDirectionalityBit(
            final byte directionality) {

        if (directionality == 0) {
            return TO_PBC;
        }

        if (directionality == 1) {
            return TO_PBS;
        }

        return null;
    }
}
