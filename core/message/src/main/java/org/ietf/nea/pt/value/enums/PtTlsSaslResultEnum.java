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
package org.ietf.nea.pt.value.enums;

/**
 * Enumeration of known SASL authentication results.
 *
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
