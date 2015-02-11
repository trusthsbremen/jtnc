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
package de.hsbremen.tc.tnc.tnccs.client;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;

/**
 * Helper to manage global handshake request retries like events.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class GlobalHandshakeRetryProxy implements GlobalHandshakeRetryListener {

    private GlobalHandshakeRetryListener listener;

    /**
     * Registers one handshake retry listener to forward global
     * handshake retries to.
     *
     * @param listener the destination handshake retry listener
     */
    public void register(final GlobalHandshakeRetryListener listener) {
        if (this.listener == null) {
            this.listener = listener;
        } else {
            throw new IllegalStateException("Listener already registered.");
        }
    }

    @Override
    public void requestGlobalHandshakeRetry(
            final ImHandshakeRetryReasonEnum reason) throws TncException {
        if (this.listener != null) {
            this.listener.requestGlobalHandshakeRetry(reason);
        } else {
            throw new TncException("Global handshake retry is not supported.",
                    TncExceptionCodeEnum.TNC_RESULT_CANT_RETRY);
        }

    }

}
