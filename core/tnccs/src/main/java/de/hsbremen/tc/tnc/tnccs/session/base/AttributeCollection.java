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
package de.hsbremen.tc.tnc.tnccs.session.base;

import java.util.HashSet;
import java.util.Set;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;

/**
 * Collection of attribute values. Implementing the same access control methods
 * like the attribute values.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class AttributeCollection implements Attributed {

    private final Set<? super Attributed> attributes;

    /**
     * Creates an attribute collection.
     */
    public AttributeCollection() {
        this.attributes = new HashSet<>();
    }

    /**
     * Adds an object that contains attributes to
     * the collection.
     *
     * @param <T> the attribute type
     * @param attributes the attributes to add
     */
    public <T extends Attributed> void add(final T attributes) {
        this.attributes.add(attributes);
    }

    /**
     * Removes an object that contains attributes from
     * the collection.
     *
     * @param <T> the attribute type
     * @param attributes the attributes to remove
     * @return true if removal was successful
     */
    public <T extends Attributed> boolean remove(final T attributes) {
        return this.attributes.remove(attributes);
    }

    @Override
    public Object getAttribute(final TncAttributeType type)
            throws TncException {
        if (attributes.isEmpty()) {
            throw new UnsupportedOperationException(
                    "The operation getAttribute(...) "
                        + "is not supported, because there "
                        + "are no attributes.");
        }

        Object o = null;
        for (Object attribut : attributes) {
            if (attribut instanceof Attributed) {
                try {
                    o = ((Attributed) attribut).getAttribute(type);
                } catch (UnsupportedOperationException | TncException e) {
                    // ignore
                }
            }
        }

        if (o != null) {
            return o;
        } else {
            throw new TncException("The attribute with ID " + type.id()
                    + " is unknown.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }

    @Override
    public void setAttribute(final TncAttributeType type, final Object value)
            throws TncException {
        boolean attributeSet = false;
        if (attributes.isEmpty()) {
            throw new UnsupportedOperationException(
                    "The operation setAttribute(...) "
                    + "is not supported, because there "
                    + "are no attributes to set.");
        }

        for (Object attribut : attributes) {
            if (attribut instanceof Attributed) {
                try {
                    ((Attributed) attribut).setAttribute(type, value);
                    attributeSet = true;
                } catch (UnsupportedOperationException | TncException e) {
                    // ignore
                }
            }
        }

        if (!attributeSet) {
            throw new TncException("The attribute with ID " + type.id()
                    + " is unknown or not writeable.",
                    TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
        }
    }

}
