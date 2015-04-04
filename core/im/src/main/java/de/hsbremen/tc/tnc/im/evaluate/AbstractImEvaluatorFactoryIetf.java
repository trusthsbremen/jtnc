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
package de.hsbremen.tc.tnc.im.evaluate;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.tnccs.TnccsAdapter;

/**
 * Generic base for an evaluator factory to compose the integrity measurement
 * components. Especially important for inheritance.
 *
 *
 */
public abstract class AbstractImEvaluatorFactoryIetf implements
        ImEvaluatorFactory {

    private ImEvaluatorManager units;

    /**
     * Creates the base for an evaluator factory.
     */
    protected AbstractImEvaluatorFactoryIetf() {
        this.units = null;
    }

    @Override
    public ImEvaluatorManager getEvaluators(final TnccsAdapter tncc,
            final ImParameter imParams) {
        if (this.units == null) {
            this.units = this.createEvaluatorManager(tncc, imParams);
        }

        return this.units;
    }

    /**
     * Creates the evaluator manager and must be overwritten.
     *
     * @param tnccs the TNC(C/S) adapter to get IDs and handle handshake retry
     * requests
     * @param imParams the generic IM(C/V) parameter set
     * @return the evaluator manager
     */
    protected abstract ImEvaluatorManager createEvaluatorManager(
            TnccsAdapter tnccs, ImParameter imParams);
}
