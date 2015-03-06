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
package de.hsbremen.tc.tnc.transport;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

/**
 * Generic TransportConnection to control an underlying connection.
 *
 */
public interface TransportConnection {

    /**
     * Returns if the connection was initiated by this side.
     *
     * @return true if connection was initiated by this side and false if not
     */
    boolean isSelfInititated();

    /**
     * Checks if the connection is open.
     *
     * @return true if connection is open and data can be written/read, false if
     * connection is closed.
     */
    boolean isOpen();

    /**
     * Opens the connection and registers a listener for incoming data.
     *
     * @param listener the listener for incoming data
     * @throws ConnectionException if open fails
     */
    void open(TnccsListener listener) throws ConnectionException;

    /**
     * Sends data via this connection.
     *
     * @param buffer the data to send
     * @throws ConnectionException if send fails
     */
    void send(ByteBuffer buffer) throws ConnectionException;

    /**
     * Closes the connection. No further data can be send. Connection cannot be
     * reopened.
     */
    void close();

    /**
     * Returns the connection's attributes.
     *
     * @return the connection's attributes
     */
    Attributed getAttributes();
}
