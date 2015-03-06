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
package de.hsbremen.tc.tnc.im.evaluate;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.TnccsAdapter;

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
