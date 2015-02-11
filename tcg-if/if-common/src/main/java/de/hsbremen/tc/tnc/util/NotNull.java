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
package de.hsbremen.tc.tnc.util;

/**
 * Utility to check if an object or objects are null.
 *
 * @author Carl-Heinz Genzel
 *
 */
public abstract class NotNull {

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

        for (Object object : objects) {
            NotNull.check(object);
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
            throw new NullPointerException(message);
        }
    }
}
