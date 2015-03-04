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
 * Enumeration of known SASL authentication results.
 *
 * @author Carl-Heinz Genzel
 *
 */
public enum PtTlsSaslResultEnum {

    /**
     * 0 - Success - SASL authentication was successful, and identity was
     * confirmed.
     *
     */
    SUCCESS(0),
    /**
     * 1 - Failure - SASL authentication failed. This might be caused by the
     * client providing an invalid user identity and/or credential pair. Note
     * that this is not the same as a mechanism's failure to process the
     * authentication as reported by the Mechanism Failure code.
     */
    FAILURE(1),
    /**
     * 2 - Abort - SASL authentication exchange was aborted by the sender.
     */
    ABORT(2),
    /**
     * 3 - Mechanism Failure - SASL "mechanism failure" during the processing of
     * the client's authentication (e.g., not related to the user's input). For
     * example, this could occur if the mechanism is unable to allocate memory
     * (e.g., malloc) that is needed to process a received SASL mechanism
     * message.
     */
    MECHANISM_FAILURE(3);

    private int id;

    /**
     * Creates a result with the given ID.
     * @param id the result ID
     */
    private PtTlsSaslResultEnum(final int id) {
        this.id = id;
    }

    /**
     * Returns the SASL result ID.
     * @return the result ID.
     */
    public int id() {
        return this.id;
    }

    /**
     * Returns a result for the given result ID.
     * @param id the result ID
     * @return a result or null
     */
    public static PtTlsSaslResultEnum fromId(final long id) {

        if (id == SUCCESS.id) {
            return SUCCESS;
        }

        if (id == FAILURE.id) {
            return FAILURE;
        }

        if (id == ABORT.id) {
            return ABORT;
        }

        if (id == MECHANISM_FAILURE.id) {
            return MECHANISM_FAILURE;
        }

        return null;
    }
}
