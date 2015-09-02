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
