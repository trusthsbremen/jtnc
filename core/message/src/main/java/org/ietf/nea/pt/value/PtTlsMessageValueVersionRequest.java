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

/**
 * IETF RFC 6876 transport version request message value.
 *
 *
 */
public class PtTlsMessageValueVersionRequest extends AbstractPtTlsMessageValue {

    private final short minVersion; // 8 bit(s)
    private final short maxVersion; // 8 bit(s)
    private final short preferredVersion; // 8 bit(s)

    /**
     * Creates the message value with the given values.
     *
     * @param length the value length
     * @param minVersion the minimum supported version
     * @param maxVersion the maximum supported version
     * @param preferredVersion the preferred version
     */
    PtTlsMessageValueVersionRequest(final long length, final short minVersion,
            final short maxVersion, final short preferredVersion) {
        super(length);
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
        this.preferredVersion = preferredVersion;
    }

    /**
     * Returns the minimum supported version.
     *
     * @return the minimum version
     */
    public short getMinVersion() {
        return this.minVersion;
    }

    /**
     * Returns the the maximum supported version.
     *
     * @return the maximum version
     */
    public short getMaxVersion() {
        return this.maxVersion;
    }

    /**
     * Returns the preferred version.
     *
     * @return the preferred version
     */
    public short getPreferredVersion() {
        return this.preferredVersion;
    }
}
