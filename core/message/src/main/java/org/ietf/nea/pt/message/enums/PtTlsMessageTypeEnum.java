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
package org.ietf.nea.pt.message.enums;


/**
 * Enumeration of known transport message types.
 *
 *
 */
public enum PtTlsMessageTypeEnum {

    /**
     * 0 - PT-Experimental - Reserved for experimental use.
     */
    IETF_PT_TLS_EXERIMENTAL(0),
    /**
     * 1 - PT-Version Request - Version negotiation request including the range
     * of versions supported by the sender.
     */
    IETF_PT_TLS_VERSION_REQUEST(1),
    /**
     * 2 - PT-Version Response - PT-TLS protocol version selected by the
     * responder.
     */
    IETF_PT_TLS_VERSION_RESPONSE(2),
    /**
     * 3 - PT-SASL Mechanisms - Sent by the NEA Server to indicate what SASL
     * mechanisms it is willing to use for authentication on this session.
     */
    IETF_PT_TLS_SASL_MECHANISMS(3),
    /**
     * 4 - PT-SASL Mechanism Selection - Sent by the NEA Client to select a SASL
     * mechanism from the list offered by the NEA Server.
     */
    IETF_PT_TLS_SASL_MECHANISM_SELECTION(4),
    /**
     * 5 - PT-SASL Authentication Data - Opaque octets exchanged between the NEA
     * Client and NEA Server's SASL mechanisms to perform the client
     * authentication.
     */
    IETF_PT_TLS_SASL_AUTHENTICATION_DATA(5),
    /**
     * 6 - PT-SASL Result - Indicates the result code of the SASL mechanism
     * authentication.
     */
    IETF_PT_TLS_SASL_RESULT(6),
    /**
     * 7 - PT-PB-TNC Batch - Contains a PB-TNC batch.
     */
    IETF_PT_TLS_PB_BATCH(7),
    /**
     * 8 - PT-Error - PT-TLS Error message.
     */
    IETF_PT_TLS_ERROR(8);

    private long id;

    /**
     * Creates the type with the given type ID.
     *
     * @param id the type ID
     */
    private PtTlsMessageTypeEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the message type ID.
     *
     * @return the type ID
     */
    public long id() {
        return this.id;
    }

    /**
     * Returns the type for the given type ID.
     *
     * @param id the type ID
     * @return a type or null
     */
    public static PtTlsMessageTypeEnum fromId(final long id) {

        if (id == IETF_PT_TLS_EXERIMENTAL.id) {
            return IETF_PT_TLS_EXERIMENTAL;
        }

        if (id == IETF_PT_TLS_VERSION_REQUEST.id) {
            return IETF_PT_TLS_VERSION_REQUEST;
        }

        if (id == IETF_PT_TLS_VERSION_RESPONSE.id) {
            return IETF_PT_TLS_VERSION_RESPONSE;
        }

        if (id == IETF_PT_TLS_SASL_MECHANISMS.id) {
            return IETF_PT_TLS_SASL_MECHANISMS;
        }

        if (id == IETF_PT_TLS_SASL_MECHANISM_SELECTION.id) {
            return IETF_PT_TLS_SASL_MECHANISM_SELECTION;
        }

        if (id == IETF_PT_TLS_SASL_AUTHENTICATION_DATA.id) {
            return IETF_PT_TLS_SASL_AUTHENTICATION_DATA;
        }

        if (id == IETF_PT_TLS_SASL_RESULT.id) {
            return IETF_PT_TLS_SASL_RESULT;
        }

        if (id == IETF_PT_TLS_PB_BATCH.id) {
            return IETF_PT_TLS_PB_BATCH;
        }

        if (id == IETF_PT_TLS_ERROR.id) {
            return IETF_PT_TLS_ERROR;
        }

        return null;
    }

}
