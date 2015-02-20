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
package de.hsbremen.tc.tnc.tnccs.im.manager;

import java.util.Set;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;

/**
 * Generic IM(C/V) manager baser to manage the IM(C/V) life cycle.
 *
 * @author Carl-Heinz Genzel
 *
 * @param <T> the managed type (e.g. IMC or IMV)
 */
public interface ImManager<T> {

    /**
     * Adds an IM(C/V) with the manager and initializes
     * it.
     *
     * @param im the IM(C/V) to register
     * @return the assigned primary ID for the IM(C/V)
     * @throws ImInitializeException if initialization fails
     */
    long add(T im) throws ImInitializeException;

    /**
     * Removes and unloads an IM(C/V) with the given
     * primary ID from the manager.
     *
     * @param id the primary IM(C/V) ID
     */
    void remove(long id);

    /**
     * Requests an additional ID for the given IM(C/V) from
     * the manager.
     *
     * @param im the IM(C/V)
     * @return the additional ID
     * @throws TncException if no additional ID is available
     */
    long reserveAdditionalId(T im) throws TncException;

    /**
     * Reports the component types, the given IM(C/V) is interested in.
     *
     * @param im the IM(C/V)
     * @param types the supported component types
     * @throws TncException if the registration of supported component types
     * fails
     */
    void reportSupportedMessagesTypes(T im,
            Set<SupportedMessageType> types) throws TncException;

    /**
     * Terminates the manager and all managed IM(C/V).
     */
    void terminate();

}
