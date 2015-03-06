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
package de.hsbremen.tc.tnc.im.adapter.connection;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

/**
 * Generic adapter base for an IM(C/V) connection.
 *
 */
public interface ImConnectionAdapter {

    /**
     * Sends a integrity measurement component message from the IM(C/V) to the
     * lower layers.
     *
     * @param component the component message
     * @param identifier the connection unique identifier for the message
     * @throws TncException if the transmission fails
     * @throws ValidationException if the serialization for the transmission
     * fails
     */
    void sendMessage(ImObjectComponent component,
            long identifier) throws TncException, ValidationException;

    /**
     * Request handshake for the connection with the given reason.
     * @param reason the reason for the request
     * @throws TncException if handshake is not acceptable
     */
    void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason)
            throws TncException;

    /**
     * Get a connection attribute by the give type.
     * @param type the attribute type
     * @return the attribute
     * @throws TncException if the attribute was not accessible
     */
    Object getAttribute(TncAttributeType type)
            throws TncException;
}
