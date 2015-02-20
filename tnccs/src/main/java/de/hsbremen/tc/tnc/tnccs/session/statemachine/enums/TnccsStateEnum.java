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
package de.hsbremen.tc.tnc.tnccs.session.statemachine.enums;

/**
 *
 * Enumeration with state names.
 *
 * @author Carl-Heinz Genzel
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
     * an no further message can be send over the related connection.
     */
    END("End"),
    /**
     * Error state indicating, that an error exists an has to be handled.
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
