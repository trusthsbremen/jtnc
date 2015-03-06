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
package de.hsbremen.tc.tnc.tnccs.im.manager;

import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.tnccs.im.manager.exception.ImInitializeException;

/**
 * Generic IM(C/V) manager baser to manage the IM(C/V) life cycle.
 *
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
     * @param primaryId the primary IM(C/V) ID
     */
    void remove(long primaryId);

    /**
     * Returns a list of managed IM(C/V).
     *
     * @return a list of IM(C/V)
     */
    Map<Long, T> getManaged();

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
