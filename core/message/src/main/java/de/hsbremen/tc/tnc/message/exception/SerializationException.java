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
package de.hsbremen.tc.tnc.message.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

/**
 * Exception signals errors while writting bytes to or reading bytes from
 * a byte containing object such as stream or byte buffer.
 *
 *
 */
public class SerializationException extends ComprehensibleException {

    private static final long serialVersionUID = 6714101079655195252L;
    private final boolean fatal;

    /**
     * Creates an exception with a specified message and optional
     * reason varargs where a fatal flag can be set to indicate that
     * the whole read or write process has failed or that only one read
     * or write operation could not be executed and further read/write
     * operations are possible.
     *
     * @param message the specified exception message
     * @param fatal true if the whole read/write process failed
     * @param reasons the optional reasons, which let to the exception
     */
    public SerializationException(final String message,
            final boolean fatal, final Object... reasons) {
        super(message, reasons);
        this.fatal = fatal;
    }

    /**
     * Creates an exception with a specified message and optional
     * reason varargs based on an existing exception where a fatal
     * flag can be set to indicate that the whole read or write process
     * has failed or that only one read or write operation could not
     * be executed and further read/write operations are possible.
     *
     * @param message the specified exception message
     * @param exception the existing exception, which has to be encapsulated
     * @param fatal true if the whole read/write process failed
     * @param reasons the optional reasons, which let to the exception
     */
    public SerializationException(final String message,
            final Throwable exception, final boolean fatal,
            final Object... reasons) {
        super(message, exception, reasons);
        this.fatal = fatal;
    }

    /**
     * Returns whether the whole read or write process failed.
     * @return the fatal flag
     */
    public boolean isFatal() {
        return this.fatal;
    }

    @Override
    public String toString() {
        return super.toString() + "SerializationException [fatal="
                + this.fatal + "]";
    }

}
