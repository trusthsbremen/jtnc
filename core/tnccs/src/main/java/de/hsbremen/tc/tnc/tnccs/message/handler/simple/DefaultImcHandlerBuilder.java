/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
