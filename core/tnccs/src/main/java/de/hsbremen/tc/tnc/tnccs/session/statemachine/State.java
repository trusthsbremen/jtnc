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
 * Generic TNC(C/S) session state to drive the TNC(C/S) state machine.
 *
 *
 */
public interface State {

    /**
     * Returns the next state to execute one of the processing methods with
     * (e.g. collect or handle).
     * The type of the message batch determines the state.
     *
     * @param result the message batch to determine the state
     * @return the state for processing
     */
    State getProcessorState(TnccsBatch result);

    /**
     * Instructs the state to collect messages as batch
     * and return the message batch.
     *
     * @return the collected messages
     */
    TnccsBatch collect();

    /**
     * Instructs the state to handle a message batch
     * and return resulting messages as batch.
     *
     * @param batchContainer the container containing the message batch
     * @return the resulting messages
     */
    TnccsBatch handle(TnccsBatchContainer batchContainer);

    /**
     * Returns the following state after one of the processing methods
     * (e.g. collect or handle) was executed.
     *
     * @return the conclusive state
     */
    State getConclusiveState();

}
