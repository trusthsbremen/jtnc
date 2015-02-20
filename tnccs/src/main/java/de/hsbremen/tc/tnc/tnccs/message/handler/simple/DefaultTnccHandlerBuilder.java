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
package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;

/**
 * Default builder to build a default message handler,
 * handling IF-TNCCS message destined for a TNCC.
 * The builder can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultTnccHandlerBuilder implements
        TnccsHandlerBuilder<TnccHandler> {

    private Attributed attributes;

    /**
     * Creates a default TNCC handler builder.
     */
    public DefaultTnccHandlerBuilder() {
        this.attributes = new AttributeCollection();
    }

    @Override
    public TnccsHandlerBuilder<TnccHandler> setAttributes(
            final Attributed attributes) {
        if (attributes != null) {
            this.attributes = attributes;
        }

        return this;
    }

    @Override
    public TnccHandler toHandler() {

        TnccHandler tncsHandler = new DefaultTnccHandler(attributes);

//        // clear side effects
//        this.attributes = new AttributeCollection();

        return tncsHandler;
    }

}
