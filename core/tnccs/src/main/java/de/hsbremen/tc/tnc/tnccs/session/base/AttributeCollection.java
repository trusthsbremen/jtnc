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
