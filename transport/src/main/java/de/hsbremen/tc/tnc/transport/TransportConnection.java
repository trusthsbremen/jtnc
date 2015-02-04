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
package de.hsbremen.tc.tnc.transport;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

/**
 * Generic TransportConnection to control an underlying connection.
 *
 * @author Carl-Heinz Genzel
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
