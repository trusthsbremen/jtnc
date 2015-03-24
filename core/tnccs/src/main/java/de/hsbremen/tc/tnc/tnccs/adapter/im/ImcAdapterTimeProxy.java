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
package de.hsbremen.tc.tnc.tnccs.adapter.im;

import java.util.concurrent.Callable;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

/**
 * IMC adapter time controlling proxy. Controls the runtime of an IMC function
 * call and cancels a function call if necessary.
 *
 *
 */
class ImcAdapterTimeProxy extends AbstractTimeProxy implements
        ImcAdapter {

    private final ImcAdapter adapter;

    /**
     * Creates the adapter proxy for a given IMC adapter.
     *
     * @param adapter the adapter to control
     * @param timeout the maximum function call runtime
     * must be > 0
     */
    public ImcAdapterTimeProxy(final ImcAdapter adapter, final long timeout) {
        super(timeout);
        this.adapter = adapter;
    }

    @Override
    public long getPrimaryId() {
        return this.adapter.getPrimaryId();
    }

    @Override
    public void notifyConnectionChange(final ImcConnectionAdapter connection,
            final TncConnectionState state)
            throws TncException, TerminatedException {
        this.adapter.notifyConnectionChange(connection, state);
    }

    @Override
    public void beginHandshake(final ImcConnectionAdapter connection)
            throws TncException, TerminatedException {
        try {
            super.execute(new Callable<Boolean>() {

                @Override
                public Boolean call() throws TncException, TerminatedException {
                    adapter.beginHandshake(connection);
                    return Boolean.TRUE;
                }

            });
        } finally {
            connection.denyMessageReceipt();
        }

    }

    @Override
    public void handleMessage(final ImcConnectionAdapter connection,
            final TnccsMessageValue message) throws TncException,
            TerminatedException {
        try {
            super.execute(new Callable<Boolean>() {

                @Override
                public Boolean call() throws TncException, TerminatedException {
                    adapter.handleMessage(connection, message);
                    return Boolean.TRUE;
                }

            });
        } finally {
            connection.denyMessageReceipt();
        }

    }

    @Override
    public void batchEnding(final ImcConnectionAdapter connection)
            throws TncException, TerminatedException {
        try {
            super.execute(new Callable<Boolean>() {

                @Override
                public Boolean call() throws TncException, TerminatedException {
                    adapter.batchEnding(connection);
                    return Boolean.TRUE;
                }

            });
        } finally {
            connection.denyMessageReceipt();
        }
    }

    @Override
    public void terminate() throws TerminatedException {
        this.adapter.terminate();
    }

    @Override
    public Object getAttribute(final TncAttributeType type)
            throws TncException {
        return this.adapter.getAttribute(type);
    }

    @Override
    public void setAttribute(final TncAttributeType type, final Object value)
            throws TncException {
        this.adapter.setAttribute(type, value);

    }

}
