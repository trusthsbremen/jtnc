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
package org.ietf.nea.pa.attribute;

/**
 * Generic IETF RFC 5792 integrity measurement attribute value base. Especially
 * important for inheritance.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class AbstractPaAttributeValue implements PaAttributeValue {

    private final long length;
    private final boolean omittable;

    /**
     * Creates the attribute value base with
     * the given value length, that is omittable.
     *
     * @param length the value length
     */
    protected AbstractPaAttributeValue(final long length) {
        this(length, true);
    }

    /**
     * Creates the attribute value base with
     * the given values.
     *
     * @param length the value length
     * @param omittable the importance of the attribute
     */
    protected AbstractPaAttributeValue(final long length,
            final boolean omittable) {
        this.length = length;
        this.omittable = omittable;
    }

    @Override
    public long getLength() {
        return this.length;
    }

    @Override
    public boolean isOmittable() {
        return omittable;
    }

}
