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
 * Generic builder to build a TNC(C/S) session state machine.
 * The builder can be used in a fluent way.
 *
 * @author Carl-Heinz Genzel
 *
 * @param <T> the state machine type based on its content handler (e.g. TNCC ->
 * TNCC content handler)
 */
public interface StateMachineBuilder<T extends TnccsContentHandler> {

    /**
     * Sets the session and/or connection attributes, which should be accessible
     * by the state machine.
     *
     * @param attributes the session/connection attributes
     * @return this builder
     */
    StateMachineBuilder<T> setAttributes(final Attributed attributes);

    /**
     * Sets the content handler, that should be used by the state machine to
     * handle messages.
     * @param contentHandler the content handler
     * @return this builder
     */
    StateMachineBuilder<T> setContentHandler(T contentHandler);

    /**
     * Sets the state helper which can be used by the states to
     * obtain necessary information.
     * @param stateHelper the state helper
     * @return this builder
     */
    StateMachineBuilder<T> setStateHelper(StateHelper<T> stateHelper);

    /**
     * Creates the session state machine.
     * @return the state machine
     */
    StateMachine toStateMachine();

}
