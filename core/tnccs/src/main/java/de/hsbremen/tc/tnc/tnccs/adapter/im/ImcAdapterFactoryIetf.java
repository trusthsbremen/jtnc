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
package de.hsbremen.tc.tnc.tnccs.adapter.im;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * The factory creates an IMC adapter according to IETF/TCG specifications.
 * The adapter can be created with a time proxy to control the runtime of
 * a function call.
 *
 *
 */
public class ImcAdapterFactoryIetf implements ImcAdapterFactory {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImcAdapterFactoryIetf.class);

    public static final long DEFAULT_TIMEOUT = 800;
    public static final long DISABLED_TIMEOUT = -1;
    
    private long timeout;

    /**
     * Creates the factory with a default function call timeout
     * of 800 msec.
     */
    public ImcAdapterFactoryIetf() {
        this(DISABLED_TIMEOUT);
    }

    /**
     * Creates the factory with the given function call timeout.
     * If timeout <= 0 no time control is added. Timeout must be
     * less than 1 sec.(1000 msec.).
     *
     * @param timeout the function call timeout
     */
    public ImcAdapterFactoryIetf(final long timeout) {
        if (timeout < HSBConstants.TCG_IM_MAX_FUNCTION_RUNTIME) {
            this.timeout = timeout;
        } else {
            throw new IllegalArgumentException(new StringBuilder()
                    .append("Timeout of ").append(timeout)
                    .append(" milliseconds is to large, ")
                    .append("timeout must be less than one second ")
                    .append("( < 1000 milliseconds).").toString());
        }
    }

    @Override
    public ImcAdapter createImcAdapter(final IMC imc, final long primaryId) {

        NotNull.check("IMC cannot be null.", imc);

        ImcAdapter adapter = null;
        if (timeout > 0) {
             adapter = new ImcAdapterTimeProxy(new ImcAdapterIetf(imc,
                primaryId), timeout);
        } else {
            adapter = new ImcAdapterIetf(imc, primaryId);
        }
        if (LOGGER.isDebugEnabled()) {
            StringBuilder b = new StringBuilder();
            b.append("IMC adapter created for IMC ").append(imc.toString())
                    .append(" with the following supported abilities: \n")
                    .append("Attribut support: ")
                    .append((imc instanceof AttributeSupport)).append("\n")
                    .append("Long support: ").append((imc instanceof IMCLong));
            LOGGER.debug(b.toString());
        }

        return adapter;
    }
}
