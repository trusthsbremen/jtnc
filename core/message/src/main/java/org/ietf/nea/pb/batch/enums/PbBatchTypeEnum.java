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
     * Type to indicate, that it has completed its evaluation. The batch MUST
     * include a PB-Assessment-Result message and MAY include a
     * PB-Access-Recommendation message.
     */
    RESULT((byte) 3),
    /**
     * 4 - CRETRY - The Posture Broker Client may send a batch with this Batch
     * Type to indicate, that it wishes to restart an exchange. A Posture
     * Broker Server MUST NOT send this Batch Type. A CRETRY batch may be empty
     * (contain no messages) if the Posture Broker Client has nothing else to
     * send.
     */
    CRETRY((byte) 4),
    /**
     * 5 - SRETRY - The Posture Broker Server may send a batch with this Batch
     * Type to indicate, that it wishes to restart the exchange. A Posture
     * Broker Client MUST NOT send this Batch Type. A SRETRY batch may be empty
     * (contain no messages) if the Posture Broker Server has nothing else to
     * send.
     */
    SRETRY((byte) 5),
    /**
     * 6 - CLOSE - The Posture Broker Server or Posture Broker Client may send a
     * batch with this Batch Type to indicate, that it is about to terminate the
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
