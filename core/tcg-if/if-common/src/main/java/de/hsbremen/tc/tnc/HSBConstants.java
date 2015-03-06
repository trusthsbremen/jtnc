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
package de.hsbremen.tc.tnc;

/**
 * Holds several constants, needed by this implementation.
 *
 *
 */
public abstract class HSBConstants {

    /**
     * Private constructor should never be invoked.
     */
    private HSBConstants() {
        throw new AssertionError();
    }

    /**
     * IANA PEN for the University of Applied Sciences Bremen.
     */
    public static final long HSB_PEN_VENDORID = 9649L;

    /**
     * Additional connection state, if connection state is unknown.
     */
    public static final long HSB_CONNECTION_STATE_UNKNOWN = -1L;

    /**
     * Unknown IMC/V ID.
     */
    public static final long HSB_IM_ID_UNKNOWN = -1L;

    /**
     * Default language for human readable strings.
     */
    public static final String HSB_DEFAULT_LANGUAGE = "Accept-Language: en";

    /**
     * Value signals that the maximum message size
     * for a transport message is unknown.
     */
    public static final long HSB_TRSPT_MAX_MESSAGE_SIZE_UNKNOWN = 0;

    /**
     * Unlimited maximum message size for a transport message.
     */
    public static final long HSB_TRSPT_MAX_MESSAGE_SIZE_UNLIMITED = 0xFFFFFFFFL;

    /**
     * Unknown maximum message size for a IF-M message.
     */
    public static final long TCG_IM_MAX_MESSAGE_SIZE_UNKNOWN = 0;

    /**
     * Unlimited maximum message size for a IF-M message.
     */
    public static final long TCG_IM_MAX_MESSAGE_SIZE_UNLIMITED = 0xFFFFFFFFL;

    /**
     * Unknown maximum round trips for a transport connection.
     */
    public static final long TCG_IM_MAX_ROUND_TRIPS_UNKNOWN = 0;

    /**
     * Unlimited maximum round trips for a transport connection.
     */
    public static final long TCG_IM_MAX_ROUND_TRIPS_UNLIMITED = 0xFFFFFFFFL;

    /**
     * Maximum value for the identifier in a message with identifier.
     */
    public static final long TCG_MAX_MESSAGE_IDENTIFIER = 0xFFFFFFFFL;

    /**
     * Maximum amount of time an IF-IM(C/V) IM(C/V) function call
     * has to finish its execution.
     */
    public static final long TCG_IM_MAX_FUNCTION_RUNTIME = 1000;
}
