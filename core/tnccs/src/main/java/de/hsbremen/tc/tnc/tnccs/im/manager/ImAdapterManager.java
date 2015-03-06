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

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.tnccs.adapter.im.ImAdapter;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

/**
 * Generic base to manage the IM(C/V) adapter life cycle.
 *
 *
 * @param <T> the managed adapter type (e.g. IMC adapter
 * or IMV adapter)
 */
public interface ImAdapterManager<T extends ImAdapter<?>> {
    /**
     * Returns a list of managed IM(C/V) adapters.
     *
     * @return a list of IM(C/V) adapters
     */
    Map<Long, T> getAdapter();

    /**
     * Returns a router which can be used to determine
     * the interested IM(C/V) for a component type message.
     *
     * @return the message router
     */
    ImMessageRouter getRouter();

    /**
     * Notifies the manager about a fatal
     * error caused by an IM(C/V), so that the manager
     * can decide what to do with the IM(C/V) (e.g.
     * reload, terminate).
     *
     * @param primaryId the primary ID
     * @param exception exception describing the fatal error
     */
    void notifyFatalError(long primaryId, TncException exception);
}
