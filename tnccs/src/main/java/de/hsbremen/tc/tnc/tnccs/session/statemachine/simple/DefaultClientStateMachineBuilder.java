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
package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachineBuilder;

/**
 * Builds a TNCC session state machine.
 * The builder can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class DefaultClientStateMachineBuilder implements
        StateMachineBuilder<TnccContentHandler> {

    private Attributed attributes;
    private TnccContentHandler contentHandler;
    private StateHelper<TnccContentHandler> clientStateFactory;

    /**
     * Creates a builder for a default TNCC session state machine.
     */
    public DefaultClientStateMachineBuilder() {
        this.attributes = new AttributeCollection();
        this.contentHandler = null;
        this.clientStateFactory = null;
    }

    @Override
    public StateMachineBuilder<TnccContentHandler> setAttributes(
            final Attributed attributes) {
        if (attributes != null) {
            this.attributes = attributes;
        }

        return this;
    }

    @Override
    public StateMachineBuilder<TnccContentHandler> setContentHandler(
            final TnccContentHandler contentHandler) {
        if (contentHandler != null) {
            this.contentHandler = contentHandler;
        }

        return this;
    }

    @Override
    public StateMachineBuilder<TnccContentHandler> setStateHelper(
            final StateHelper<TnccContentHandler> clientStateFactory) {
        if (clientStateFactory != null) {
            this.clientStateFactory = clientStateFactory;
            this.contentHandler = null;
            this.attributes = null;
        }

        return this;
    }

    @Override
    public StateMachine toStateMachine() {

        StateMachine machine = null;

        if (this.clientStateFactory != null) {
            machine = new DefaultClientStateMachine(clientStateFactory);
        } else {
            if (this.attributes != null && this.contentHandler != null) {
                machine = new DefaultClientStateMachine(
                        new DefaultClientStateHelper(attributes,
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
        this.clientStateFactory = null;

        return machine;
    }

}
