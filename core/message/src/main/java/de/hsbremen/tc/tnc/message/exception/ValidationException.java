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
 * Exception signals that a message or message value is not valid.
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
