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
package de.hsbremen.tc.tnc.tnccs.session.statemachine.enums;

/**
 *
 * Enumeration of state names.
 *
 *
 */
public enum TnccsStateEnum {

    /**
     * Initial state indicating, that a connection was just created.
     */
    INIT("Init"),
    /**
     * Server working state indicating, that the TNCS is currently working.
     */
    SERVER_WORKING("Server Working"),
    /**
     * Client working state indication, that the TNCC is currently working.
     */
    CLIENT_WORKING("Client Working"),
    /**
     * Decided state indicating, that an integrity handshake was finished and a
     * decision (result) exists.
     */
    DECIDED("Decided"),
    /**
     * End state indicating, that the state machine has reached its final state
     * and no further message can be send over the related connection.
     */
    END("End"),
    /**
     * Error state indicating, that an error exists and has to be handled.
     */
    ERROR("Error"),
    /**
     * Retry state indicating, that a handshake retry will be executed.
     */
    RETRY("Retry");

    private String value;

    /**
     * Creates a state with a name value.
     *
     * @param value the name value
     */
    private TnccsStateEnum(final String value) {
        this.value = value;
    }

    /**
     * Returns the state name value.
     *
     * @return the state name
     */
    public String value() {
        return this.value;
    }

}
