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
package de.hsbremen.tc.tnc.message;

/**
 * Generic helper to enable arbitrary message related objects to be stacked
 * together via add and remove operations.
 *
 * @author Carl-Heinz Genzel
 *
 * @param <T> the object type that will be stacked
 */
public interface Combined<T> {

    /**
     *
     * Adds an object to the combined type for the related message type with the
     * given vendor ID and and type ID.
     *
     * @param vendorId the message vendor ID
     * @param messageType the message type ID
     * @param component the object to add
     */
    void add(Long vendorId, Long messageType, T component);

    /**
     *
     * Removes the object from the combined type for the related message type
     * with the given vendor ID and and type ID.
     *
     * @param vendorId the message vendor ID
     * @param messageType the message type ID
     */
    void remove(Long vendorId, Long messageType);

}
