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
package de.hsbremen.tc.tnc.exception.enums;

/**
 * Enumeration of known result codes for exceptions at the IF-IMC/V interface.
 *
 * @author Carl-Heinz Genzel
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
