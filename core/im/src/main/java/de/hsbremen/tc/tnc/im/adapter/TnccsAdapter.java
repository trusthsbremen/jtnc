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
package de.hsbremen.tc.tnc.im.adapter;

import java.util.Set;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

/**
 * Generic base adapter for TNC(C/S).
 *
 */
public interface TnccsAdapter {

    /**
     * Returns a listener to request global handshake retries.
     * @return the handshake retry listener
     */
    GlobalHandshakeRetryListener getHandshakeRetryListener();

    /**
     * Submits a list of supported component messages to the TNC(C/S).
     * @param supportedTypes the list of supported component message types
     * @throws TncException if message types are not valid or an local exception
     * occurred
     */
    void reportMessageTypes(
            Set<SupportedMessageType> supportedTypes) throws TncException;

    /**
     * Reserves an additional ID from the TNC(C/S) for the IM(C/V).
     * @return the additional ID
     * @throws TncException if no additional ID can be allocated
     */
    long reserveAdditionalId() throws TncException;

}
