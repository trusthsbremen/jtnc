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
package de.hsbremen.tc.tnc.message.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

/**
 * Exception that signals that a rule was violated by a checked value.
 *
 *
 */
public class RuleException extends ComprehensibleException {

    public static final int NOT_SPECIFIED = 0;

    private static final long serialVersionUID = -860068945081676249L;

    private final boolean fatal;
    private final long errorCode;
    private final int errorCause;

    /**
     * Creates an exception with a specified message and optional
     * reason varargs. The other values describe the exception for
     * further handling.
     *
     * @param message the specified exception message
     * @param fatal true if the exception is fatal an message processing
     * must be stopped
     * @param errorCode a defined code classifying the exception
     * @param errorCause a second qualifier to describe the exception
     * in addition to the classification more precisely
     * @param reasons the optional reasons, which let to the exception
     */
    public RuleException(final String message, final boolean fatal,
            final long errorCode, final int errorCause,
            final Object... reasons) {
        super(message, reasons);
        this.fatal = fatal;
        this.errorCode = errorCode;
        this.errorCause = errorCause;
    }

    /**
     * Creates an exception with a specified message and optional
     * reason varargs based on an existing exception. The other
     * values describe the exception for further handling.
     *
     * @param message the specified exception message
     * @param fatal true if the exception is fatal an message processing
     * must be stopped
     * @param errorCode a defined code classifying the exception
     * @param errorCause a second qualifier to describe the exception
     * in addition to the classification more precisely
     * @param exception the existing exception, which has to be encapsulated
     * @param reasons the optional reasons, which let to the exception
     */
    public RuleException(final String message, final Throwable exception,
            final boolean fatal, final long errorCode, final int errorCause,
            final Object... reasons) {
        super(message, exception, reasons);
        this.fatal = fatal;
        this.errorCode = errorCode;
        this.errorCause = errorCause;
    }

    /**
     * Returns whether the exception is fatal and message
     * processing should be stopped.
     *
     * @return true if the exception is fatal
     */
    public boolean isFatal() {
        return this.fatal;
    }

    /**
     * Returns the error code classifying the exception.
     * @return the error code
     */
    public long getErrorCode() {
        return this.errorCode;
    }

    /**
     * Returns the error cause qualifier that describes the exception
     * in addition to the classification more precisely.
     *
     * @return the error cause qualifier
     */
    public int getErrorCause() {
        return this.errorCause;
    }

    @Override
    public String toString() {
        return super.toString() + "ValidationException [fatal=" + this.fatal
                + ", errorCode=" + this.errorCode + ", errorCause="
                + this.errorCause + "]";
    }

}
