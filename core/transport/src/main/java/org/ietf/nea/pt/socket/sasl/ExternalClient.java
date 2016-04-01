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
package org.ietf.nea.pt.socket.sasl;

import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/**
 * Implements the EXTERNAL SASL client-side mechanism. (<A
 * HREF="http://www.ietf.org/rfc/rfc2222.txt">RFC 2222</A>).
 * It processes no callback.
 */
public class ExternalClient implements SaslClient {

    private static final String MECH_NAME = "EXTERNAL";
    private boolean completed;

    private final String authorizationId;

    /**
     * Constructs an External mechanism without authorization ID.
     * 
     */
    public ExternalClient() {
        this(null);
    }
    
    /**
     * Constructs an External mechanism with optional authorization ID.
     * 
     * @param authorizationId If non-null, used to specify authorization ID
     */
    public ExternalClient(final String authorizationId) {
        
        this.authorizationId = authorizationId;
        this.completed = false;
    }

    @Override
    public String getMechanismName() {
        return MECH_NAME;
    }

    @Override
    public boolean hasInitialResponse() {
        // this mechanism as an initial response
        return true;
    }

    @Override
    public void dispose() throws SaslException {
        // Do nothing.
    }

    @Override
    public byte[] evaluateChallenge(final byte[] challengeData)
            throws SaslException {
        if (completed) {
            throw new IllegalStateException(
                    "EXTERNAL authentication already completed");
        }

        completed = true;

        byte[] authzid = null;

        try {
            authzid = (this.authorizationId != null) ? this.authorizationId
                    .getBytes("UTF8") : new byte[0];
        } catch (java.io.UnsupportedEncodingException e) {
            throw new SaslException("PLAIN no UTF-8 encoding", e);
        }

        return authzid;
    }

    @Override
    public boolean isComplete() {
        return completed;
    }

    @Override
    public byte[] unwrap(final byte[] incoming, final int offset,
            final int len) throws SaslException {
        if (completed) {
            throw new SaslException(
                    "EXTERNAL supports neither integrity nor privacy.");
        } else {
            throw new IllegalStateException(
                    "EXTERNAL authentication Not completed.");
        }
    }

    @Override
    public byte[] wrap(final byte[] outgoing, final int offset, final int len)
            throws SaslException {
        if (completed) {
            throw new SaslException(
                    "EXTERNAL supports neither integrity nor privacy.");
        } else {
            throw new IllegalStateException(
                    "EXTERNAL authentication not completed.");
        }
    }

    @Override
    public Object getNegotiatedProperty(final String propName) {
        if (completed) {
            return null;
        } else {
            throw new IllegalStateException(
                    "EXTERNAL authentication not completed");
        }
    }

}
