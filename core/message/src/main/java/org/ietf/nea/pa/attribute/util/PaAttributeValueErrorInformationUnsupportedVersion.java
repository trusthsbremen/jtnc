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
package org.ietf.nea.pa.attribute.util;

/**
 * IETF RFC 5792 integrity measurement unsupported version error information.
 *
 *
 */
public class PaAttributeValueErrorInformationUnsupportedVersion extends
        AbstractPaAttributeValueErrorInformation {

    private final short maxVersion;
    private final short minVersion;

    /**
     * Creates the error information with the given values.
     *
     * @param length the information length
     * @param messageHeader the dumped message header
     * @param maxVersion the maximum supported version
     * @param minVersion the minimum supported version
     */
    PaAttributeValueErrorInformationUnsupportedVersion(final long length,
            final MessageHeaderDump messageHeader, final short maxVersion,
            final short minVersion) {
        super(length, messageHeader);

        this.maxVersion = maxVersion;
        this.minVersion = minVersion;
    }

    /**
     * Returns the maximum supported version.
     *
     * @return the maximum supported version
     */
    public short getMaxVersion() {
        return this.maxVersion;
    }

    /**
     * Returns the minimum supported version.
     *
     * @return the minimum supported version
     */
    public short getMinVersion() {
        return this.minVersion;
    }

}
