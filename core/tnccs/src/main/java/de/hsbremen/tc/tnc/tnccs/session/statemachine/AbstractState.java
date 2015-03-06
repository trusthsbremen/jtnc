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

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;

/**
 * Generic TNC(C/S) session state base. Especially important for inheritance.
 * Implements all methods as unsupported operation. Methods must be
 * overwritten to work properly. Holds the successor state.
 *
 *
 */
public abstract class AbstractState implements State {

    private State successor;

    /**
     * Creates a generic base for a session state.
     */
    protected AbstractState() {
        this.successor = null;
    }

    @Override
    public State getProcessorState(final TnccsBatch result) {
        throw new UnsupportedOperationException(
                "getProccessorState() is not supported by this state.");
    }

    @Override
    public TnccsBatch collect() {
        throw new UnsupportedOperationException(
                "collect() is not supported by this state.");
    }

    @Override
    public TnccsBatch handle(final TnccsBatchContainer batchContainer) {
        throw new UnsupportedOperationException(
                "handle() is not supported by this state.");
    }

    @Override
    public State getConclusiveState() {
        throw new UnsupportedOperationException(
                "getConclusiveState() is not supported by this state.");
    }

    /**
     * Returns the successor state if set.
     * @return the successor state or null if not set
     */
    protected State getSuccessor() {
        return this.successor;
    }

    /**
     * Sets the successor state.
     * @param successor the successor state
     */
    protected void setSuccessor(final State successor) {
        this.successor = successor;
    }
}
