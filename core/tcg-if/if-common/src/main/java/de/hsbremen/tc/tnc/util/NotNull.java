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
package de.hsbremen.tc.tnc.util;

/**
 * Utility to check if an object or objects are null.
 *
 *
 */
public final class NotNull {

    /**
     * Private constructor should never be invoked.
     */
    private NotNull() {
        throw new AssertionError();
    }

    /**
     * Checks if object is null.
     * @param object the object to check
     * @throws NullPointerException if object is null
     */
    public static void check(final Object object) {
        if (object == null) {
            throw new NullPointerException("Object cannot be null.");
        }
    }

    /**
     * Checks if object is null.
     *
     * @param message the message to use for the exception message
     * @param object the object to check
     * @throws NullPointerException if object is null
     */
    public static void check(final String message, final Object object) {
        try {
            NotNull.check(object);
        } catch (NullPointerException e) {
            throw new NullPointerException(message);
        }
    }

    /**
     * Checks if one of the objects is null.
     * @param objects the objects to check
     * @throws NullPointerException at the first occurrence of null
     */
    public static void check(final Object...objects) {
        if (objects == null) {
            throw new NullPointerException("Objects cannot be null.");
        }

        for (int i = 0; i < objects.length; i++) {
            try {
                NotNull.check(objects[i]);
            } catch (NullPointerException e) {
                throw new NullPointerException("The "
                        + (i + 1)
                        + ". object was null. "
                        + e.getMessage());
            }
        }
    }

    /**
     * Checks if one of the objects is null.
     *
     * @param message the message to use for the exception message
     * @param objects the objects to check
     * @throws NullPointerException at the first occurrence of null
     */
    public static void check(final String message, final Object...objects) {
        try {
            NotNull.check(objects);
        } catch (NullPointerException e) {
            throw new NullPointerException(message + " " + e.getMessage());
        }
    }
}
