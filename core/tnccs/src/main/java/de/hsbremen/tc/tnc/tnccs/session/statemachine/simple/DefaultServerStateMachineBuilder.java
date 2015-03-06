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
package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachineBuilder;

/**
 * Builds a TNCS session state machine. The builder can be used in a fluent way.
 *
 *
 */
public class DefaultServerStateMachineBuilder implements
        StateMachineBuilder<TncsContentHandler> {

    private Attributed attributes;
    private TncsContentHandler contentHandler;
    private StateHelper<TncsContentHandler> serverStateFactory;

    /**
     * Creates a builder for a default TNCS session state machine.
     */
    public DefaultServerStateMachineBuilder() {
        this.attributes = new AttributeCollection();
        this.contentHandler = null;
        this.serverStateFactory = null;
    }

    @Override
    public StateMachineBuilder<TncsContentHandler> setAttributes(
            final Attributed attributes) {
        if (attributes != null) {
            this.attributes = attributes;
        }

        return this;
    }

    @Override
    public StateMachineBuilder<TncsContentHandler> setContentHandler(
           final TncsContentHandler contentHandler) {
        if (contentHandler != null) {
            this.contentHandler = contentHandler;
        }

        return this;
    }

    @Override
    public StateMachineBuilder<TncsContentHandler> setStateHelper(
            final StateHelper<TncsContentHandler> serverStateFactory) {
        if (serverStateFactory != null) {
            this.serverStateFactory = serverStateFactory;
            this.contentHandler = null;
            this.attributes = null;
        }

        return this;
    }

    @Override
    public StateMachine toStateMachine() {

        StateMachine machine = null;

        if (this.serverStateFactory != null) {
            machine = new DefaultServerStateMachine(serverStateFactory);
        } else {
            if (this.attributes != null && this.contentHandler != null) {
                machine = new DefaultServerStateMachine(
                        new DefaultServerStateHelper(attributes,
                                contentHandler));
            } else {
                throw new IllegalStateException(
                        "Not all necessary attributes set. "
                        + "A state helper or the attributes and "
                        + "the content handler must be set first.");
            }
        }

        // remove side effects
        this.attributes = new AttributeCollection();
        this.contentHandler = null;
        this.serverStateFactory = null;

        return machine;
    }

}
