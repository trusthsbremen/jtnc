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
package de.hsbremen.tc.tnc.exception;

import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;

/**
 * Exception signals exceptions that are thrown at the IF-IMC/V interface.
 *
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
