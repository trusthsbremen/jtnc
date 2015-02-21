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
 * @author Carl-Heinz Genzel
 *
 */
public class ImcAdapterTimeProxy extends AbstractTimeProxy implements
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
