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
 * Exception that signals that a message or message value is not valid.
 *
 *
 */
public class ValidationException extends ComprehensibleException {

    public static final long OFFSET_NOT_SET = Long.MIN_VALUE;

    private static final long serialVersionUID = 6714101079655195252L;
    private final long exceptionOffset;

    /**
     * Creates an exception with a specified message and optional reason varargs
     * based on an existing exception. The existing exception is thrown by a
     * validation rule. An offset is used to indicate the position of the
     * invalid value in dependence to the message representation (e.g. byte
     * position, if a byte representation is used).
     *
     * @param message the specified exception message
     * @param exception the existing exception, which has to be encapsulated
     * @param exceptionOffset the position of the invalid value
     * @param reasons the optional reasons, which let to the exception
     */
    public ValidationException(final String message,
            final RuleException exception, final long exceptionOffset,
            final Object... reasons) {
        super(message, exception, reasons);
        this.exceptionOffset = exceptionOffset;
    }

    /**
     * Returns the position of the invalid value.
     * @return the position of the invalid value
     */
    public long getExceptionOffset() {
        return this.exceptionOffset;
    }

    @Override
    public synchronized RuleException getCause() {
        // must be RuleException because of the constructor
        return (RuleException) super.getCause();
    }

    @Override
    public String toString() {
        return super.toString() + "SerializationException [exceptionOffset="
                + this.exceptionOffset + "]";
    }

}
