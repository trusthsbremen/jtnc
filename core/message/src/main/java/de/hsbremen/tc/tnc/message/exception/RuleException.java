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
package de.hsbremen.tc.tnc.message.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

/**
 * Exception that signals that a rule was violated by a checked value.
 *
 * @author Carl-Heinz Genzel
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
     * @param errorCause a second qualifier to describe the exception besides
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
