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
package org.ietf.nea;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Helper object to handle byte arrays providing
 * some copy and conversion methods.
 *
 *
 */
public abstract class ByteArrayHelper {

    /**
     * Private constructor should never be invoked.
     */
    private ByteArrayHelper() {
        throw new AssertionError();
    }

    /**
     * Converts the given byte array with maximum length 8
     * to a long value.
     * @param b the byte array
     * @return the long value
     */
    public static long toLong(final byte[] b) {
        if (b == null || b.length > 8) {
            throw new IllegalArgumentException("Supplied array is to large.");
        }
        long value = 0L;
        for (int i = 0; i < b.length; i++) {
            value = (value << 8) + (b[i] & 0xFF);
        }

        return value;
    }

    /**
     * Converts the given byte array with maximum length 2
     * to a short value.
     * @param b the byte array
     * @return the short value
     */
    public static short toShort(final byte[] b) {
        if (b == null || b.length > 2) {
            throw new IllegalArgumentException("Supplied array is to large.");
        }
        short value = 0;
        for (int i = 0; i < b.length; i++) {
            value = (short) ((value << 8) + (b[i] & 0xFF));
        }

        return value;
    }

    /**
     * Converts the given byte array with maximum length 4
     * to an int value.
     * @param b the byte array
     * @return the int value
     */
    public static int toInt(final byte[] b) {
        if (b == null || b.length > 4) {
            throw new IllegalArgumentException("Supplied array is to large.");
        }
        int value = 0;
        for (int i = 0; i < b.length; i++) {
            value = (int) ((value << 8) + (b[i] & 0xFF));
        }

        return value;
    }

    /**
     * Merges the first byte array with the second one and
     * returns the result.
     * @param first the first byte array
     * @param second the second byte array
     * @return the merged array
     */
    public static byte[] mergeArrays(final byte[] first, final byte[] second) {
        byte[] merged = new byte[first.length + second.length];
        System.arraycopy(first, 0, merged, 0, first.length);
        System.arraycopy(second, 0, merged, first.length, second.length);
        return merged;
    }

    /**
     * Reads a number of bytes (length) into an array
     * from the given stream.
     * @param in the stream to read from
     * @param length the number of bytes to read
     * @return the resulting array of bytes
     * @throws IOException if read fails or number of bytes is not
     * equal to the given length
     *
     */
    public static byte[] arrayFromStream(final InputStream in, final int length)
            throws IOException {
        byte[] buffer = new byte[0];

        if (length > 0) {
            buffer = new byte[length];
        }

        int count = 0;

        count = in.read(buffer); // blocks until data is available or stream is
                                 // closed

        if (count >= length) {
            // shorten the array to the actual data
            return Arrays.copyOf(buffer, count);
        } else {
            throw new IOException(
                    "Stream must be closed, not enough data to read.");
        }

    }
}
