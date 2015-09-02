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
package de.hsbremen.tc.tnc.exception.enums;

/**
 * Enumeration of known result codes for exceptions at the IF-IMC/V interface.
 *
 *
 */
public enum TncExceptionCodeEnum {

    /**
     * The IMC or IMV's initialize method has not been called.
     */
    TNC_RESULT_NOT_INITIALIZED(1L),

    /**
     * The IMC or IMV's initialize method was called twice without
     * a call to the IMC or IMV's terminate method.
     */

    TNC_RESULT_ALREADY_INITIALIZED(2L),

    /**
     * TNCC or TNCS cannot attempt handshake retry.
     */
    TNC_RESULT_CANT_RETRY(4L),

    /**
     * TNCC or TNCS refuses to attempt handshake retry.
     */

    TNC_RESULT_WONT_RETRY(5L),

    /**
     * Method parameter is not valid.
     */

    TNC_RESULT_INVALID_PARAMETER(6L),

    /**
     * IMC or IMV cannot respond now.
     */

    TNC_RESULT_CANT_RESPOND(7L),

    /**
     * Illegal operation attempted.
     */
    TNC_RESULT_ILLEGAL_OPERATION(8L),

    /**
     * Unspecified error.
     */
    TNC_RESULT_OTHER(9L),

    /**
     * Unspecified fatal error.
     */
    TNC_RESULT_FATAL(10L),

    /**
     * Exceeded maximum round trips supported by the underlying protocol.
     */
    TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS(0x00559700L),

    /**
     * Exceeded maximum message size supported by the underlying protocol.
     */
    TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE(0x00559701L),

    /**
     * Connection does not support long message types.
     */
    TNC_RESULT_NO_LONG_MESSAGE_TYPES(0x00559702L),

    /**
     * Connection does not support SOH.
     */
    TNC_RESULT_NO_SOH_SUPPORT(0x00559703L);

    private final long id;

    /**
     * Creates an exception code enumeration value with the ID.
     * @param id the result ID
     */
    private TncExceptionCodeEnum(final long id) {
        this.id = id;
    }

    /**
     * Returns the result ID.
     * @return the result ID
     */
    public long id() {
        return id;
    }

    /**
     * Returns an exception code for the given result ID.
     *
     * @param id the result ID
     * @return the exception code or null
     */
    public static TncExceptionCodeEnum fromId(final long id) {

        if (id == TNC_RESULT_NOT_INITIALIZED.id) {
            return TNC_RESULT_NOT_INITIALIZED;
        }

        if (id == TNC_RESULT_ALREADY_INITIALIZED.id) {
            return TNC_RESULT_ALREADY_INITIALIZED;
        }

        if (id == TNC_RESULT_CANT_RETRY.id) {
            return TNC_RESULT_CANT_RETRY;
        }

        if (id == TNC_RESULT_WONT_RETRY.id) {
            return TNC_RESULT_WONT_RETRY;
        }

        if (id == TNC_RESULT_INVALID_PARAMETER.id) {
            return TNC_RESULT_INVALID_PARAMETER;
        }

        if (id == TNC_RESULT_CANT_RESPOND.id) {
            return TNC_RESULT_CANT_RESPOND;
        }

        if (id == TNC_RESULT_ILLEGAL_OPERATION.id) {
            return TNC_RESULT_ILLEGAL_OPERATION;
        }

        if (id == TNC_RESULT_FATAL.id) {
            return TNC_RESULT_FATAL;
        }

        if (id == TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS.id) {
            return TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS;
        }

        if (id == TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE.id) {
            return TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE;
        }

        if (id == TNC_RESULT_NO_LONG_MESSAGE_TYPES.id) {
            return TNC_RESULT_NO_LONG_MESSAGE_TYPES;
        }

        if (id == TNC_RESULT_NO_SOH_SUPPORT.id) {
            return TNC_RESULT_NO_SOH_SUPPORT;
        }

        return TNC_RESULT_OTHER;
    }

}
