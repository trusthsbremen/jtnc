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
package org.ietf.nea.pt.value.enums;

/**
 * Enumeration of known transport message error codes.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PtTlsMessageErrorCodeEnum {

    /**
     * 0 - Reserved - Reserved value indicates that the PT-TLS Error message
     * SHOULD be ignored by all recipients. This MAY be used for debugging
     * purposes to allow a sender to see a copy of the message that was
     * received.
     */
    IETF_RESERVED(0),
    /**
     * 1 - Malformed Message - PT-TLS message unrecognized or unsupported. This
     * error code SHOULD be sent when the basic message content sanity test
     * fails. The sender of this error code MUST consider it a fatal error and
     * abort the assessment.
     */
    IETF_MALFORMED_MESSAGE(1),
    /**
     * 2 - Version Not Supported - This error SHOULD be sent when a PT-TLS
     * Responder receives a PT-TLS Version Request message containing a range of
     * version numbers that doesn't include any version numbers that the
     * recipient is willing and able to support on the session. All PT-TLS
     * messages carrying the Version Not Supported error code MUST use a version
     * number of 1. All parties that receive or send PT-TLS messages MUST be
     * able to properly process an error message that meets this description,
     * even if they cannot process any other aspect of PT-TLS version 1. The
     * sender and receiver of this error code MUST consider it a fatal error and
     * close the TLS session after sending or receiving this PT-TLS message.
     */
    IETF_UNSUPPORTED_VERSION(2),
    /**
     * 3 - Type Not Supported - PT-TLS Message Type unknown or not supported.
     * When a recipient receives a PT-TLS Message Type that it does not support,
     * it MUST send back this error, ignore the message, and proceed. For
     * example, this could occur if the sender used a Vendor ID for the Message
     * Type that is not supported by the recipient. This error message does not
     * indicate that a fatal error has occurred, so the assessment is allowed to
     * continue.
     */
    IETF_UNSUPPORTED_MESSAGE_TYPE(3),
    /**
     * 4 - Invalid Message - PT-TLS message received was invalid based on the
     * protocol state. For example, this error would be sent if a recipient
     * receives a message associated with the PT-TLS Negotiation Phase (such as
     * Version messages) after the protocol has reached the PT-TLS Data
     * Transport Phase. The sender and receiver of this error code MUST consider
     * it a fatal error and close the TLS session after sending or receiving
     * this PT-TLS message.
     */
    IETF_INVALID_MESSAGE(4),
    /**
     * 5 - SASL Mechanism Error - A fatal error occurred while trying to perform
     * the client authentication. For example, the NEA Client is unable to
     * support any of the offered SASL mechanisms. The sender and receiver of
     * this error code MUST consider it a fatal error and close the TLS session
     * after sending or receiving this PT-TLS message.
     */
    IETF_SASL_MECHANISM_ERROR(5),
    /**
     * 6 - Invalid Parameter The PT-TLS Error message sender has received a
     * message with an invalid or unsupported value in the PT-TLS header. This
     * could occur if the NEA Client receives a PT-TLS message from the NEA
     * Server with a Message Length of zero (see Section 3.5 for details). The
     * sender and receiver of this error code MUST consider it a fatal error and
     * close the TLS session after sending or receiving this PT-TLS message.
     */
    IETF_INVALID_PARAMETER(6);

    private int code;

    /**
     * Creates an error code with the given code
     * value.
     * @param code the code value
     */
    private PtTlsMessageErrorCodeEnum(final int code) {
        this.code = code;
    }

    /**
     * Returns the value of the error code.
     * @return the error code value
     */
    public int code() {
        return this.code;
    }

    /**
     * Returns an error code for the given code value.
     * @param code the code value
     * @return an error code or null
     */
    public static PtTlsMessageErrorCodeEnum fromCode(final int code) {

        if (code == IETF_RESERVED.code) {
            return IETF_RESERVED;
        }

        if (code == IETF_MALFORMED_MESSAGE.code) {
            return IETF_MALFORMED_MESSAGE;
        }

        if (code == IETF_UNSUPPORTED_VERSION.code) {
            return IETF_UNSUPPORTED_VERSION;
        }

        if (code == IETF_UNSUPPORTED_MESSAGE_TYPE.code) {
            return IETF_UNSUPPORTED_MESSAGE_TYPE;
        }

        if (code == IETF_INVALID_MESSAGE.code) {
            return IETF_INVALID_MESSAGE;
        }

        if (code == IETF_SASL_MECHANISM_ERROR.code) {
            return IETF_SASL_MECHANISM_ERROR;
        }

        if (code == IETF_INVALID_PARAMETER.code) {
            return IETF_INVALID_PARAMETER;
        }

        return null;
    }
}
