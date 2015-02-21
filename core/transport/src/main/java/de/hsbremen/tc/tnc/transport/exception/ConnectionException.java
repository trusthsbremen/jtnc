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
package de.hsbremen.tc.tnc.transport.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

/**
 * Exception signals exceptions that are thrown by a TransportConnection.
 *
 * @author Carl-Heinz Genzel
 */
public class ConnectionException extends ComprehensibleException {

    private static final long serialVersionUID = -8801198662406661211L;

    /**
     * Creates an exception with a specified message and optional
     * reason varargs.
     *
     * @param message the specified exception message
     * @param reasons the optional reasons, which let to the exception
     */
    public ConnectionException(final String message, final Object... reasons) {
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
    public ConnectionException(final String message, final Throwable exception,
            final Object... reasons) {
        super(message, exception, reasons);
    }

}
