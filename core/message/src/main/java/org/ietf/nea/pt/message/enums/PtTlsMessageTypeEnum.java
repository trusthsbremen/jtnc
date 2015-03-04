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
package org.ietf.nea.pt.message.enums;


/**
 * Enumeration of known transport message types.
 *
 * @author Carl-Heinz Genzel
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
