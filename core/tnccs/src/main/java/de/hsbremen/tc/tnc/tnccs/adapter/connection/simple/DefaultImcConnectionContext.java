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
package de.hsbremen.tc.tnc.tnccs.adapter.connection.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.AbstractImConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection
.ConnectionHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;

/**
 * Default IMC connection context.
 *
 *
 */
public class DefaultImcConnectionContext extends AbstractImConnectionContext
        implements ImcConnectionContext {

    /**
     * Creates a default IMC connection context with the given
     * common session and/or connection attributes and the handshake
     * retry listener.
     *
     * @param attributes the common session/connection attributes
     * @param listener the handshake retry listener
     */
    public DefaultImcConnectionContext(final Attributed attributes,
            final ConnectionHandshakeRetryListener listener) {
        super(attributes, listener);
    }

}
