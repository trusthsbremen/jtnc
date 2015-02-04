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
package de.hsbremen.tc.tnc;

/**
 * Holds several constants, needed by this implementation.
 *
 * @author Carl-Heinz Genzel
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
     * Maximum value for the identifier in a transport message.
     */
    public static final long TCG_TRSPT_MAX_MESSAGE_IDENTIFIER = 0xFFFFFFFFL;
}
