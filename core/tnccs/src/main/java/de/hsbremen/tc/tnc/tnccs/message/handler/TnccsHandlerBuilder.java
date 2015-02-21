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
package de.hsbremen.tc.tnc.tnccs.message.handler;

import de.hsbremen.tc.tnc.attribute.Attributed;

/**
 * Generic builder to build a message handler,
 * handling IF-TNCCS message destined for a TNC(C/S).
 * The builder can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 * @param <T> the handler type (e.g. TNCC or TNCS handler)
 */
public interface TnccsHandlerBuilder<T> extends HandlerBuilder<T> {

    /**
     * Sets the accessible session and/or connection attributes used
     * to build the handler.
     * @param attributes the session/connection attributes
     * @return this builder
     */
    TnccsHandlerBuilder<T> setAttributes(Attributed attributes);

}
