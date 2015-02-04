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
package de.hsbremen.tc.tnc.exception;

import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;

/**
 * Exception signals exceptions that are thrown at the IF-IMC/V interface.
 *
 * @author Carl-Heinz Genzel
 */
public class TncException extends ComprehensibleException {

    private static final long serialVersionUID = 230606945788180582L;
    private final TncExceptionCodeEnum mResultCode;

    /**
     * Creates a fully-specified TncException from a given
     * client TNCException object.
     *
     * @param exception the client TNCException
     */
    public TncException(
            final org.trustedcomputinggroup.tnc.ifimc.TNCException exception) {
        this((exception.getMessage() == null || exception.getMessage()
                .isEmpty()) ? "No message was specified." : exception
                .getMessage(), exception, TncExceptionCodeEnum
                .fromId(exception.getResultCode()));
    }

    /**
     * Creates a fully-specified TncException from a given
     * server TNCException object.
     *
     * @param exception the server TNCException
     */
    public TncException(
            final org.trustedcomputinggroup.tnc.ifimv.TNCException exception) {
        this((exception.getMessage() == null || exception.getMessage()
                .isEmpty()) ? "No message was specified." : exception
                .getMessage(), exception, TncExceptionCodeEnum
                .fromId(exception.getResultCode()));
    }

    /**
     * Creates a exception with a specified message, a result code and optional
     * reason varargs.
     *
     * @param message the specified exception message
     * @param resultCode the result code
     * @param reasons the optional reasons, which let to the exception
     */
    public TncException(final String message,
            final TncExceptionCodeEnum resultCode, final Object... reasons) {
        super(message, reasons);
        this.mResultCode = resultCode;
    }

    /**
     * Creates an exception with a specified message, a result code and optional
     * reason varargs based on an existing exception.
     *
     * @param message the specified exception message
     * @param exception the existing exception, which has to be encapsulated
     * @param resultCode the resultCode code
     * @param reasons the optional reasons, which let to the exception
     */
    public TncException(final String message, final Throwable exception,
            final TncExceptionCodeEnum resultCode, final Object... reasons) {
        super(message, exception, reasons);
        this.mResultCode = resultCode;
    }

    /**
     * Returns the result code for the exception object.
     *
     * @return the result code
     */
    public TncExceptionCodeEnum getResultCode() {
        return mResultCode;
    }
}
