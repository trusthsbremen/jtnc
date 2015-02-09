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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * Generic base for an evaluation unit. Especially important for inheritance.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class AbstractImEvaluationUnitIetf implements ImEvaluationUnit {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractImEvaluationUnitIetf.class);

    private final GlobalHandshakeRetryListener globalHandshakeRetryListener;

    /**
     * Creates the base for an evaluation unit with the given handshake retry
     * listener.
     *
     * @param globalHandshakeRetryListener the global handshake retry listener.
     */
    protected AbstractImEvaluationUnitIetf(
            final GlobalHandshakeRetryListener globalHandshakeRetryListener) {
        this.globalHandshakeRetryListener = globalHandshakeRetryListener;
    }

    /**
     * Requests a global handshake retry for all current connections with the
     * given reason.
     *
     * @param reason the reason for the request
     * @throws TncException if handshake is not acceptable
     */
    protected void requestGlobaleHandshakeRetry(
            final ImHandshakeRetryReasonEnum reason) throws TncException {
        this.globalHandshakeRetryListener.requestGlobalHandshakeRetry(reason);
    }
}
