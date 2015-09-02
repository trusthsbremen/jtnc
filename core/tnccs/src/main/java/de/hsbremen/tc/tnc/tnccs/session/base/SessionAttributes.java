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
package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.message.TcgProtocolBindingIdentifier;

/**
 * Common attributes for every TNC(C/S) session. Attributes are accessible
 * thru getters as well as thru Attributed.
 *
 */
public interface SessionAttributes extends Attributed {

    /**
     * Returns the IF-TNCCS protocol identifier (e.g. IF-TNCCS 2.0).
     * @return the IF-TNCCS protocol identifier
     */
    TcgProtocolBindingIdentifier getTnccsProtocolIdentifier();


    /**
     * Returns the current message round trip count of
     * the session.
     * @return the current round trips
     */
    long getCurrentRoundTrips();

    /**
     * Returns the preferred language for human readable
     * strings contained in messages.
     *
     * @return the preferred language
     */
    String getPreferredLanguage();

    /**
     * Sets the current message round trip count of the
     * session. If the round trips exceed the maximum value of
     * long the current round trips will be set to zero.
     *
     * @param roundtrips the current round trips
     */
    void setCurrentRoundTrips(long roundtrips);

    /**
     * Sets the preferred language for human readable strings
     * contained in messages.
     *
     * @param preferredLanguage the preferred language
     */
    void setPreferredLanguage(String preferredLanguage);

}
