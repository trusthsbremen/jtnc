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

import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapterFactory;
import de.hsbremen.tc.tnc.tnccs.adapter.connection
.ImvConnectionAdapterFactoryIetf;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.connection
.simple.DefaultImvConnectionContext;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImvAdapter;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImAdapterManager;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandlerBuilder;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * Default builder to build a default message handler,
 * handling IF-TNCCS message destined for an IMV.
 * The builder can be used in a fluent way.
 *
 *
 */
public class DefaultImvHandlerBuilder implements ImvHandlerBuilder {

    private final ImAdapterManager<ImvAdapter> adapterManager;
    private ImvConnectionContext connectionContext;

    /**
     * Creates a default IMV handler builder with the given IMV
     * adapter manager.
     * @param adapterManager the IMV manager
     */
    public DefaultImvHandlerBuilder(
            final ImAdapterManager<ImvAdapter> adapterManager) {
        NotNull.check("IMV sdapter manager arguments cannot "
                + "be null.", adapterManager);
        this.adapterManager = adapterManager;
        this.connectionContext = new DefaultImvConnectionContext(
                new AttributeCollection(), null);
    }

    @Override
    public ImvHandlerBuilder setConnectionContext(
            final ImvConnectionContext connectionContext) {
        if (connectionContext != null) {
            this.connectionContext = connectionContext;
        }

        return this;
    }

    @Override
    public ImvHandler toHandler() {

        ImvConnectionAdapterFactory connectionFactory =
                new ImvConnectionAdapterFactoryIetf(connectionContext);

        ImvHandler imvHandler = new DefaultImvHandler(adapterManager,
                connectionFactory, connectionContext,
                adapterManager.getRouter());

//        // clear side effects
//        this.connectionContext = new DefaultImvConnectionContext(
//                new AttributeCollection(), null);

        return imvHandler;
    }

}
