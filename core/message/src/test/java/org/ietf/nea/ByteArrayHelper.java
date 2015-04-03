/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright (c) 2015, Carl-Heinz Genzel
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
public final class ByteArrayHelper {

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
