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
package org.ietf.nea.pb.batch.enums;

/**
 * Enumeration of known TNCCS batch types.
 *
 *
 */
public enum PbBatchTypeEnum {

    /**
     * 1 - CDATA - The Posture Broker Client may send a batch with this Batch
     * Type to convey messages to the Posture Broker Server. A Posture Broker
     * Server MUST NOT send this Batch Type. A CDATA batch may be empty (contain
     * no messages) if the Posture Broker Client has nothing to send.
     */
    CDATA((byte) 1),
    /**
     * 2 - SDATA - The Posture Broker Server may send a batch with this Batch
     * Type to convey messages to the Posture Broker Client. A Posture Broker
     * Client MUST NOT send this Batch Type. An SDATA batch may be empty
     * (contain no messages) if the Posture Broker Server has nothing to send.
     */
    SDATA((byte) 2),
    /**
     * 3 - RESULT - The Posture Broker Server may send a batch with this Batch
     * Type to indicate that it has completed its evaluation. The batch MUST
     * include a PB-Assessment-Result message and MAY include a
     * PB-Access-Recommendation message.
     */
    RESULT((byte) 3),
    /**
     * 4 - CRETRY - The Posture Broker Client may send a batch with this Batch
     * Type to indicate that it wishes to restart an exchange. A Posture Broker
     * Server MUST NOT send this Batch Type. A CRETRY batch may be empty
     * (contain no messages) if the Posture Broker Client has nothing else to
     * send.
     */
    CRETRY((byte) 4),
    /**
     * 5 - SRETRY - The Posture Broker Server may send a batch with this Batch
     * Type to indicate that it wishes to restart the exchange. A Posture Broker
     * Client MUST NOT send this Batch Type. A SRETRY batch may be empty
     * (contain no messages) if the Posture Broker Server has nothing else to
     * send.
     */
    SRETRY((byte) 5),
    /**
     * 6 - CLOSE - The Posture Broker Server or Posture Broker Client may send a
     * batch with this Batch Type to indicate that it is about to terminate the
     * underlying PT connection. A CLOSE batch may be empty (contain no
     * messages) if there is nothing to send. However, if the termination is due
     * to a fatal error, then the CLOSE batch MUST contain a PB-Error message.
     */
    CLOSE((byte) 6);

    private byte id;

    /**
     * Creates the type with the given type ID.
     *
     * @param id the type ID
     */
    private PbBatchTypeEnum(final byte id) {
        this.id = id;
    }

    /**
     * Returns the batch type ID.
     *
     * @return the type ID
     */
    public byte id() {
        return this.id;
    }

    /**
     * Returns the type for the given type ID.
     * @param id the type ID
     * @return a type enumeration or null
     */
    public static PbBatchTypeEnum fromId(final byte id) {

        if (id == CDATA.id) {
            return CDATA;
        }

        if (id == SDATA.id) {
            return SDATA;
        }

        if (id == RESULT.id) {
            return RESULT;
        }

        if (id == CRETRY.id) {
            return CRETRY;
        }

        if (id == SRETRY.id) {
            return SRETRY;
        }

        if (id == CLOSE.id) {
            return CLOSE;
        }

        return null;
    }

}
