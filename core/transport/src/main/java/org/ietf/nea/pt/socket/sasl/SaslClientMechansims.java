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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.SaslClient;

/**
 * Container for a Selection of SASL client-side authentication
 * mechanisms, that are ready to be used for authentication.
 */
public class SaslClientMechansims implements SaslMechanismSelection {
    
    private Map<String, SaslClient> mechanisms;

    /**
     * Creates an empty container for SASL client-side mechanisms.
     */
    public SaslClientMechansims() {
       
        this.mechanisms = new HashMap<String, SaslClient>();
    }
    
    /**
     * Returns the number of contained mechanisms.
     * @return the number of contained mechanisms
     */
    public int getMechanismCount() {
        return this.mechanisms.size();
    }
    
    /**
     * Adds a SASL client-side mechanism to the container.
     *  
     * @param client the SASL client-side mechanism
     * @throws IllegalArgumentException if mechanism is already completed and
     * not applicable for authentication
     */
    public void addMechanism(final SaslClient client) {
        if (client.isComplete()) {
            throw new IllegalArgumentException(
                    "Client was already used. Hence it cannot be added."); 
        }
        this.mechanisms.put(client.getMechanismName(), client);
       
    }
    
    /**
     * Returns a contained mechanism by name.
     * @param mechanismName the name of the mechanism (e.g. PLAIN)
     * @return the named mechanism or null if non is found
     */
    public SaslClient getMechanism(final String mechanismName) {
        return this.mechanisms.get(mechanismName);
    }
    
    /**
     * Returns all mechanisms in an unmodifiable map of names and mechanisms.
     *
     * @return the map of mechanisms (Map{{Name, Mechanism}*})
     */
    public Map<String, SaslClient> getAllMechansims() {
        
        return Collections.unmodifiableMap(mechanisms);
    }
}
