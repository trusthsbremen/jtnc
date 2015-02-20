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
package de.hsbremen.tc.tnc.tnccs.im.route.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

/**
 * Exception signals that no IM(C/V) was found as recipient for a message
 * type.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class NoRecipientFoundException extends ComprehensibleException {

    private static final long serialVersionUID = -8822234918021533007L;

    /**
     * Creates an exception with a specified message and optional
     * reason varargs. Usually the vendor ID followed by the message
     * type ID and if exclusive delivery was used the recipient ID
     * should be provided as reasons.
     *
     * @param message the specified exception message
     * @param reasons the optional reasons, which let to the exception
     */
    public NoRecipientFoundException(final String message,
            final Object... reasons) {
        super(message, reasons);
    }

    /**
     * Creates an exception with a specified message and optional
     * reason varargs based on an existing exception. Usually the vendor ID
     * followed by the message type ID and if exclusive delivery was used
     * the recipient ID should be provided as reasons.
     *
     * @param message the specified exception message
     * @param exception the existing exception, which has to be encapsulated
     * @param reasons the optional reasons, which let to the exception
     */
    public NoRecipientFoundException(final String message,
            final Throwable exception,
            final Object... reasons) {
        super(message, exception, reasons);
    }

}
