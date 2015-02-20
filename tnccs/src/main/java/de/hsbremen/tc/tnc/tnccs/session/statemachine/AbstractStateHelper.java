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
package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;

/**
 * Generic helper base to make information and commonly used functions
 * accessible to a TNC(C/S) state. Especially important for inheritance.
 *
 * @author Carl-Heinz Genzel
 *
 * @param <T> the helper type based on its content handler (e.g. TNCC -> TNCC
 * content handler)
 */
public abstract class AbstractStateHelper<T extends TnccsContentHandler>
        implements StateHelper<T> {

    private final T contentHandler;
    private final Attributed attributes;

    /**
     * Creates a generic base for a state helper with the given session
     * and/or connection attributes, which should be accessible by a state and
     * a content handler, that provides message handling functions.
     *
     * @param attributes the session/connection attributes
     * @param contentHandler the content handler
     */
    protected AbstractStateHelper(final Attributed attributes,
            final T contentHandler) {
        if (attributes == null) {
            throw new NullPointerException("Attributes cannot be null.");
        }
        if (contentHandler == null) {
            throw new NullPointerException("Content handler cannot be null.");
        }
        this.attributes = attributes;
        this.contentHandler = contentHandler;
    }

    @Override
    public Attributed getAttributes() {
        return this.attributes;
    }

    @Override
    public T getHandler() {
        return this.contentHandler;
    }
}
