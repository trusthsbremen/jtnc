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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Generic exception, that can hold further objects to assess the reason for an
 * exception.
 *
 */
public abstract class ComprehensibleException extends Exception {

    private static final long serialVersionUID = 6714101079655195252L;
    private final Object[] reasonArguments;

    /**
     * Creates an exception with a specified message and optional
     * reason varargs.
     *
     * @param message the specified exception message
     * @param reasons the optional reasons, which let to the exception
     */
    protected ComprehensibleException(final String message,
            final Object... reasons) {
        super(message);
        this.reasonArguments = reasons;
    }

    /**
     * Creates an exception with a specified message and optional
     * reason varargs based on an existing exception.
     *
     * @param message the specified exception message
     * @param exception the existing exception, which has to be encapsulated
     * @param reasons the optional reasons, which let to the exception
     */
    protected ComprehensibleException(final String message,
            final Throwable exception, final Object... reasons) {
        super(message, exception);
        this.reasonArguments = reasons;
    }

    /**
     * Returns the exception reasons.
     * @return the list of reasons.
     */
    public List<Object> getReasons() {
        return Collections.unmodifiableList(Arrays
                .asList((reasonArguments != null) ? reasonArguments
                        : new Object[0]));
    }

}
