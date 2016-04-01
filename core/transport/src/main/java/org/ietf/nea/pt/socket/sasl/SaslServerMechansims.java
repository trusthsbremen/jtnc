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
import java.util.Map.Entry;
import java.util.Set;

import javax.security.sasl.SaslServer;

/**
 * Container for a Selection of SASL server-side authentication
 * mechanisms, that are ready to be used for authentication.
 * The container supports authentication stages which are executed
 * one after another for authentication.
 */
public class SaslServerMechansims implements SaslMechanismSelection {
    
    private Map<Byte, Map<String, SaslServer>> mechanisms;

    /**
     * Creates an empty container for SASL server-side mechanisms.
     */
    public SaslServerMechansims() {
       
        this.mechanisms = new HashMap<Byte, Map<String, SaslServer>>();
    }
    
    /**
     * Returns the number of current authentication stages.
     * @return the number of currently available stages
     */
    public byte getStageCount() {
        return (byte) this.mechanisms.size();
    }
    
    /**
     * Returns the number of contained mechanisms for the given stage.
     * 
     * @param stage the given stage 
     * @return the number of contained mechanisms for the given stage
     */
    public int getStageMechanismCount(final byte stage) {
        Byte key = new Byte(stage);
        if (this.mechanisms.containsKey(key)) {
            return this.mechanisms.get(key).size();
        } else {
            return 0;
        }
    }
    
    /**
     * Adds a SASL sever-side mechanism to a stage of the container. Stages
     * have to be added in sequential order (e.g. 1,2,3,...). 
     * 
     * @param stage the stage the mechanism should be added to
     * @param server the SASL server-side mechanism
     * @throws IllegalArgumentException if mechanism is already completed and
     * not applicable for authentication or the given stage number is not valid
     */
    public void addMechanismToStage(final byte stage, final SaslServer server) {
        if (server.isComplete()) {
            throw new IllegalArgumentException("Server was already used."
                    + " Hence it cannot be added."); 
        }
        
        Byte tempStage = new Byte(stage);
        
        if (tempStage.byteValue() <= 0) {
            throw new IllegalArgumentException(
                    "Stage numbers must be greater or equal to 1.");
        }
        
        if (tempStage.byteValue() > 1) {
            if (!this.mechanisms.containsKey(
                    new Byte((byte) (tempStage.byteValue() - 1)))) {
                
                throw new IllegalStateException(
                        "Stages must be added in a consecutive way.");
            }
        }
        
        if (!this.mechanisms.containsKey(tempStage)) {
            this.mechanisms.put(tempStage, new HashMap<String, SaslServer>());
        }
        
        this.mechanisms.get(tempStage).put(server.getMechanismName(), server);
       
    }
    
    /**
     * Returns all mechanisms in an unmodifiable map of names and mechanisms
     * for a given stage.
     * 
     * @param stage the given stage  
     * @return the map of mechanisms (key = name, value = mechanism) of the
     * given state or null if stage does not exist
     */
    public Map<String, SaslServer> getAllMechanismsByStage(final byte stage) {
        Byte key = new Byte(stage);
        if (this.mechanisms.containsKey(key)) {
            return Collections.unmodifiableMap(this.mechanisms.get(key));
        } else {
            return null;
        }
        
    }
    
    /**
     * Returns a contained mechanism by name of a given stage.
     * 
     * @param stage the given stage
     * @param mechanismName the name of the mechanism (e.g. PLAIN)  
     * @return the named mechanism or null if non is found
     */
    public SaslServer getMechanismByStage(final byte stage,
            final String mechanismName) {
        Byte key = new Byte(stage);
        if (this.mechanisms.containsKey(key)) {
            return this.mechanisms.get(key).get(mechanismName);
        } else {
            return null;
        }
        
    }
    
    /**
     * Returns all mechanisms in an unmodifiable map of stages, names
     * and mechanisms.
     *
     * @return the map of mechanisms (Map{Stage{{Name,Mechanism}+}*})
     */
    public Map<Byte, Map<String, SaslServer>> getAllMechansims() {
        Map<Byte, Map<String, SaslServer>> unmodifiable = new HashMap<>();
        
        Set<Entry<Byte, Map<String, SaslServer>>> entrySet =
                this.mechanisms.entrySet();
        for (Entry<Byte, Map<String, SaslServer>> entry : entrySet) {
            unmodifiable.put(
                    entry.getKey(),
                    Collections.unmodifiableMap(entry.getValue())
            );
        }
        
        return Collections.unmodifiableMap(unmodifiable);
    }
}
