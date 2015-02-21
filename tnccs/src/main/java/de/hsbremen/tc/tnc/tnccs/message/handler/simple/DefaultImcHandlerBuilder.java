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

import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection
.ImcConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.simple
.DefaultImcConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImcAdapter;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default builder to build a default message handler,
 * handling IF-TNCCS message destined for an IMC.
 * The builder can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultImcHandlerBuilder implements ImcHandlerBuilder {

    private final ImAdapterManager<ImcAdapter> adapterManager;
    private ImcConnectionContext connectionContext;

    /**
     * Creates a default IMC handler builder with the given IMC
     * adapter manager.
     * @param adapterManager the IMC manager
     */
    public DefaultImcHandlerBuilder(
            final ImAdapterManager<ImcAdapter> adapterManager) {
        NotNull.check("IMC sdapter manager arguments cannot "
                + "be null.", adapterManager);
        this.adapterManager = adapterManager;
        this.connectionContext = new DefaultImcConnectionContext(
                new AttributeCollection(), null);
    }

    @Override
    public ImcHandlerBuilder setConnectionContext(
            final ImcConnectionContext connectionContext) {
        if (connectionContext != null) {
            this.connectionContext = connectionContext;
        }

        return this;
    }

    @Override
    public ImcHandler toHandler() {

        final ImcConnectionAdapterFactory connectionFactory =
                new ImcConnectionAdapterFactoryIetf(connectionContext);
        ImcHandler imvHandler = new DefaultImcHandler(adapterManager,
                connectionFactory, connectionContext,
                adapterManager.getRouter());

//        // clear side effects
//        this.connectionContext = new DefaultImcConnectionContext(
//                new AttributeCollection(), null);

        return imvHandler;
    }

}
