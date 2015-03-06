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
package de.hsbremen.tc.tnc.tnccs.adapter.tnccs;

import org.trustedcomputinggroup.tnc.ifimv.IMVLong;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * TNCS adapter factory, that creates TNCS adapter
 * according to the IETF/TCG specifications.
 *
 *
 */
public class TncsAdapterFactoryIetf implements TncsAdapterFactory {

    private final GlobalHandshakeRetryListener listener;

    /**
     * Creates a TNCS adapter factory with the given handshake retry
     * handler for global handshake retries.
     *
     * @param listener the handshake retry handler
     */
    public TncsAdapterFactoryIetf(final GlobalHandshakeRetryListener listener) {
        NotNull.check("Global handshake listener cannot be null.", listener);
        this.listener = listener;
    }

    @Override
    public TNCS createTncs(final IMV imv, final Attributed attributes,
            final ImManager<IMV> manager) {

        NotNull.check("None of the provided arguments to build a TNCS adapter"
                + "can be null.", imv, attributes, manager);

        if (imv instanceof IMVLong) {
            return new TncsAdapterIetfLong(manager, attributes, this.listener);
        } else {
            return new TncsAdapterIetf(manager, attributes, this.listener);
        }

    }

}
