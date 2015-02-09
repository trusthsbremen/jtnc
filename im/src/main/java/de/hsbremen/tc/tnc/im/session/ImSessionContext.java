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
package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * Generic IM(C/V) session parameter set. It contains values that are specific
 * for the underlying connection.
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface ImSessionContext {

    /**
     * Returns the current connection state.
     *
     * @return the connection state
     */
    TncConnectionState getConnectionState();

    /**
     * Returns a connection specific attribute identified by the type.
     *
     * @param type the attribute type
     * @return the attribute of the given type
     * @throws TncException if attribute does not exist or is not readable
     */
    Object getAttribute(TncAttributeType type)
            throws TncException;

    /**
     * Requests a handshake retry for the underlying connection with the given
     * reason.
     *
     * @param reason the reason for the request.
     */
    void requestConnectionHandshakeRetry(
            ImHandshakeRetryReasonEnum reason);

}
