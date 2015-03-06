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

import org.ietf.nea.pa.attribute.enums
.PaAttributeFactoryDefaultPasswordStatusEnum;

/**
 * IETF RFC 5792 integrity measurement factory default password status attribute
 * value.
 *
 *
 */
public class PaAttributeValueFactoryDefaultPasswordEnabled extends
        AbstractPaAttributeValue {

    private final PaAttributeFactoryDefaultPasswordStatusEnum
        factoryDefaultPasswordSet; // 32 bit(s)

    /**
     * Creates the attribute value with the given values.
     * @param length the value length
     * @param status the status
     */
    PaAttributeValueFactoryDefaultPasswordEnabled(final long length,
            final PaAttributeFactoryDefaultPasswordStatusEnum status) {
        super(length);

        this.factoryDefaultPasswordSet = status;
    }

    /**
     * Returns the factory default password status.
     * @return the factory default password status
     */
    public PaAttributeFactoryDefaultPasswordStatusEnum
        getFactoryDefaultPasswordStatus() {
        return this.factoryDefaultPasswordSet;
    }

    @Override
    public String toString() {
        return "PaAttributeValueFactoryDefaultPasswordEnabled"
                + " [factoryDefaultPasswordSet="
                + this.factoryDefaultPasswordSet.toString() + "]";
    }
}
