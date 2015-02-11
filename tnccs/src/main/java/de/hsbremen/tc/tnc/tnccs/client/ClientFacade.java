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

import de.hsbremen.tc.tnc.tnccs.client.enums.ConnectionChangeType;
import de.hsbremen.tc.tnc.transport.TransportConnection;

/**
 * Facade for an NA(A/R) to submit new connections and
 * connection changes to a TNC(S/C).
 *
 * @author Carl-Heinz Genzel
 *
 */
public interface ClientFacade {

    /**
     * Notifies the TNC(C/S) about a connection change
     * of the given connection.
     *
     * @param connection a connection from the NA(A/R)
     * @param change the change type
     */
    void notifyConnectionChange(TransportConnection connection,
            ConnectionChangeType change);

    /**
     * Notifies the TNC(C/S) about a global change
     * concerning all connections.
     *
     * @param change the change type
     */
    void notifyGlobalConnectionChange(
            ConnectionChangeType change);

    /**
     * Starts the client facade.
     */
    void start();

    /**
     * Stops the client facade.
     */
    void stop();

}
