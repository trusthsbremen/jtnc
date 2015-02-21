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

/**
 * Common attributes for every TransportConnection. Attributes are accessible
 * thru getters as well as thru Attributed.
 *
 * @author Carl-Heinz Genzel
 */
public interface TransportAttributes extends Attributed {

    /**
     * Returns the ID.
     *
     * @return the ID of a TransportConnection
     */
    String getTransportId();

    /**
     * Returns the version (e.g. V1.0).
     *
     * @return the version of TransportConnection
     */
    String getTransportVersion();

    /**
     * Returns the protocol type (e.g. TLS).
     *
     * @return the protocol type of a TransportConnection
     */
    String getTransportProtocol();

    /**
     * Return the maximum full message length.
     *
     * @return the maximum full message length of a TransportConnection
     */
    long getMaxMessageLength();

    /**
     * Returns the maximum message length of one IF-M message.
     *
     * @return the maximum message length of one IF-M message
     */
    long getMaxMessageLengthPerIm();

    /**
     * Returns the maximum round trips for an integrity handshake supported by a
     * TransportConnection.
     *
     * @return the maximum round trips of a TransportConnection
     */
    long getMaxRoundTrips();
}
