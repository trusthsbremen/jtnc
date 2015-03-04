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
package de.hsbremen.tc.tnc.connection;

/**
 * Enumeration of known connection states.
 *
 * @author Carl-Heinz Genzel
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
