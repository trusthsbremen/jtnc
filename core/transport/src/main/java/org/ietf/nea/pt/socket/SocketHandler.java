/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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
package org.ietf.nea.pt.socket;

import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

/**
 * Generic socket handler to hold a socket and mask its functions
 * for the management of the contained socket.
 */
public interface SocketHandler {

    /**
     * Checks if the contained socket is open.
     *
     * @return true if socket is open and data can be written/read, false
     * if socket is closed.
     */
    boolean isOpen();

    /**
     * Initializes the socket by executing basic operations needed before any
     * message exchange.
     * 
     * @throws ConnectionException if socket is broken or closed
     */
    void initialize() throws ConnectionException;

    /**
     * Closes the socket. No further data can be send. Socket cannot be
     * reopened.
     */
    void close();

    /**
     * Returns the transport messages received thru the socket since
     * initialization or last reset.
     *
     * @return the number of messages received
     */
    long getRxCounter();

    /**
     * Returns the messages transmitted thru the socket since
     * initialization or last reset.
     *
     * @return the number of messages transmitted
     */
    long getTxCounter();

    /**
     * Resets the counters for the messages received and transmitted to zero.
     */
    void resetCounters();

}
