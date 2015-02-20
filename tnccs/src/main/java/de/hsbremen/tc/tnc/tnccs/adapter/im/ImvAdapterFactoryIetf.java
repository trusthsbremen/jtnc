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
package de.hsbremen.tc.tnc.tnccs.adapter.im;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.IMVLong;
import org.trustedcomputinggroup.tnc.ifimv.IMVTNCSFirst;

import de.hsbremen.tc.tnc.HSBConstants;

/**
 * The factory creates an IMC adapter according to IETF/TCG specifications.
 * The adapter can be created with a time proxy to control the runtime of
 * a function call.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class ImvAdapterFactoryIetf implements ImvAdapterFactory {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ImvAdapterFactory.class);

    public static final long DEFAULT_TIMEOUT = 800;

    private long timeout;

    /**
     * Creates the factory with a default function call timeout
     * of 800 msec.
     */
    public ImvAdapterFactoryIetf() {
        this(DEFAULT_TIMEOUT);
    }

    /**
     * Creates the factory with the given function call timeout.
     * If timeout <= 0 no time control is added. Timeout must be
     * less than 1 sec.(1000 msec.).
     *
     * @param timeout the function call timeout
     */
    public ImvAdapterFactoryIetf(final long timeout) {
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
    public ImvAdapter createImvAdapter(final IMV imv, final long primaryId) {

        if (imv == null) {
            throw new NullPointerException("IMV cannot be null.");
        }

        ImvAdapter adapter = null;
        if (timeout > 0) {
             adapter = new ImvAdapterTimeProxy(new ImvAdapterIetf(imv,
                primaryId), timeout);
        } else {
            adapter = new ImvAdapterIetf(imv, primaryId);
        }

        if (LOGGER.isDebugEnabled()) {
            StringBuilder b = new StringBuilder();
            b.append("IMV adapter created for IMC ").append(imv.toString())
                    .append(" with the following supported abilities: \n")
                    .append("TNCS first support: ")
                    .append((imv instanceof IMVTNCSFirst)).append("\n")
                    .append("Long support: ").append((imv instanceof IMVLong))
                    .append("\n");
            LOGGER.debug(b.toString());
        }

        return adapter;
    }

}
