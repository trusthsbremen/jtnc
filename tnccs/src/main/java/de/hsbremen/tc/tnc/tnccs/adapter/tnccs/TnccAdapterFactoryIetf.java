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
package de.hsbremen.tc.tnc.tnccs.adapter.tnccs;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.IMCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;
import de.hsbremen.tc.tnc.util.NotNull;

/**
 * TNCC adapter factory, that creates TNCC adapter
 * according to the IETF/TCG specifications.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class TnccAdapterFactoryIetf implements TnccAdapterFactory {

    private final GlobalHandshakeRetryListener listener;

    /**
     * Creates a TNCC adapter factory with the given handshake retry
     * handler for global handshake retries.
     *
     * @param listener the handshake retry handler
     */
    public TnccAdapterFactoryIetf(final GlobalHandshakeRetryListener listener) {
        NotNull.check("Global handshake listener cannot be null.", listener);
        this.listener = listener;
    }

    @Override
    public TNCC createTncc(final IMC imc, final Attributed attributes,
            final ImManager<IMC> manager) {
        NotNull.check("None of the provided arguments to build a TNCC adapter"
                + "can be null.", imc, attributes, manager);
        if (imc instanceof IMCLong) {
            return new TnccAdapterIetfLong(manager, attributes, this.listener);
        } else {
            return new TnccAdapterIetf(manager, attributes, this.listener);
        }

    }

}
