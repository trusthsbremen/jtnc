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

import org.ietf.nea.pa.attribute.PaAttributeHeader;

/**
 * IETF RFC 5792 integrity measurement unsupported attribute error information.
 *
 *
 */
public class PaAttributeValueErrorInformationUnsupportedAttribute extends
        AbstractPaAttributeValueErrorInformation {

    private final PaAttributeHeader attributeHeader;

    /**
     * Creates the error information with the given values.
     *
     * @param length the information length
     * @param messageHeader the dumped message header
     * @param attributeHeader the attribute header copy from the unsupported
     * attribute
     */
    PaAttributeValueErrorInformationUnsupportedAttribute(final long length,
            final MessageHeaderDump messageHeader,
            final PaAttributeHeader attributeHeader) {
        super(length, messageHeader);
        this.attributeHeader = attributeHeader;
    }

    /**
     * Retruns the copy of the attribute header from the unsupported attribute.
     *
     * @return the attribute header copy
     */
    public PaAttributeHeader getAttributeHeader() {
        return this.attributeHeader;
    }

}
