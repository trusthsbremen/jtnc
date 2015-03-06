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
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;

/**
 * Common attributes for every TransportConnection. Attributes are accessible
 * thru getters as well as thru Attributed.
 *
 */
public interface TransportAttributes extends Attributed {

    /**
     * Returns the ID.
     *
     * @return the ID of a TransportConnection
     */
    String getTransportId();

    /**
     * Returns the protocol identifier (e.g. TLS 1.0).
     *
     * @return the protocol of TransportConnection
     */
    TcgProtocolBindingIdentifier getTransportProtocolIdentifier();

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
