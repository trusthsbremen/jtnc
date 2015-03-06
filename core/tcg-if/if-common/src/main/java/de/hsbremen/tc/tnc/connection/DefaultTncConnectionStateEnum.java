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
package de.hsbremen.tc.tnc.connection;

/**
 * Enumeration of known connection states.
 *
 *
 */
public enum DefaultTncConnectionStateEnum implements TncConnectionState {

    /**
     * Network connection created.
     */

    TNC_CONNECTION_STATE_CREATE(0L),

    /**
     * Handshake about to start.
     */

    TNC_CONNECTION_STATE_HANDSHAKE(1L),

    /**
     * Handshake completed. TNC Server recommended that requested access be
     * allowed.
     */

    TNC_CONNECTION_STATE_ACCESS_ALLOWED(2L),

    /**
     * Handshake completed. TNC Server recommended that isolated access be
     * allowed.
     */

    TNC_CONNECTION_STATE_ACCESS_ISOLATED(3L),

    /**
     * Handshake completed. TNCS Server recommended that no network access be
     * allowed.
     */

    TNC_CONNECTION_STATE_ACCESS_NONE(4L),

    /**
     * About to delete network connection. Remove all associated state.
     */

    TNC_CONNECTION_STATE_DELETE(5L),

    /**
     * Not information about network connection available. Only internally used,
     * if TNCC does not support information about the network connection.
     * This value is specified by this implementation and not a known state.
     */
    HSB_CONNECTION_STATE_UNKNOWN(-1L);

    private long id;

    /**
     * Creates a connection state enumeration value with ID.
     * @param id the state ID
     */
    private DefaultTncConnectionStateEnum(final long id) {
        this.id = id;
    }

    @Override
    public long id() {
        return this.id;
    }

}
