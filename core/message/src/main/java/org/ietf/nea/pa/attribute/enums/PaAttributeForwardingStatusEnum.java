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
package org.ietf.nea.pa.attribute.enums;

/**
 * Enumeration of known integrity measurement traffic forwarding status values.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PaAttributeForwardingStatusEnum {

    /**
     * 0 - Disabled - Endpoint is not forwarding traffic.
     */
    DISABLED(0),
    /**
     * 1 - Enabled - Endpoint is forwarding traffic.
     */
    ENABLED(1),
    /**
     * 2 - Unknown - Unable to determine whether endpoint is forwarding traffic.
     */
    UNKNWON(2);

    private long id;

    /**
     * Creates the traffic forwarding status with the given status ID.
     *
     * @param id the status ID
     */
    private PaAttributeForwardingStatusEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the traffic forwarding status ID.
     *
     * @return the status ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns the traffic forwarding status for the given status ID.
     *
     * @param id the status ID
     * @return a status or null
     */
    public static PaAttributeForwardingStatusEnum fromId(final long id) {
        if (id == DISABLED.id) {
            return DISABLED;
        }
        if (id == ENABLED.id) {
            return ENABLED;
        }
        if (id == UNKNWON.id) {
            return UNKNWON;
        }

        return null;
    }
}
