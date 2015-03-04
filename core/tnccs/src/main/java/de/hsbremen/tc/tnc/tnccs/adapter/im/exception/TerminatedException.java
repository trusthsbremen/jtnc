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
package de.hsbremen.tc.tnc.tnccs.adapter.im.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

/**
 * Exception signals that an IM(C/V) is terminated and should be
 * removed.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class TerminatedException extends ComprehensibleException {

    private static final long serialVersionUID = 724450380083142006L;

    /**
     * Creates an exception with a specified message and optional
     * reason varargs.
     *
     * @param message the specified exception message
     * @param reasons the optional reasons, which let to the exception
     */
    public TerminatedException(final String message, final Object... reasons) {
        super(message, reasons);
    }

    /**
     * Creates an exception with a specified message and optional
     * reason varargs based on an existing exception.
     *
     * @param message the specified exception message
     * @param exception the existing exception, which has to be encapsulated
     * @param reasons the optional reasons, which let to the exception
     */
    public TerminatedException(final String message, final Throwable exception,
            final Object... reasons) {
        super(message, exception, reasons);
    }

}