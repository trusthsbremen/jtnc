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

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageValue;
import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionAdapter;
import de.hsbremen.tc.tnc.tnccs.adapter.im.exception.TerminatedException;

/**
 * IMV adapter time controlling proxy. Controls the runtime of an IMV function
 * call and cancels a function call if necessary.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class ImvAdapterTimeProxy extends AbstractTimeProxy implements
        ImvAdapter {

    private final ImvAdapter adapter;

    /**
     * Creates the adapter proxy for a given IMC adapter.
     *
     * @param adapter the adapter to control
     * @param timeout the maximum function call runtime
     * must be > 0
     */
    public ImvAdapterTimeProxy(final ImvAdapter adapter, final long timeout) {
        super(timeout);
        this.adapter = adapter;
    }

    @Override
    public long getPrimaryId() {
        return this.adapter.getPrimaryId();
    }

    @Override
    public void notifyConnectionChange(final ImvConnectionAdapter connection,
            final TncConnectionState state) throws TncException,
            TerminatedException {
        this.adapter.notifyConnectionChange(connection, state);
    }

    @Override
    public void beginHandshake(final ImvConnectionAdapter connection)
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
    public void handleMessage(final ImvConnectionAdapter connection,
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
    public void batchEnding(final ImvConnectionAdapter connection)
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
    public void solicitRecommendation(final ImvConnectionAdapter connection)
            throws TncException, TerminatedException {
        try {
            super.execute(new Callable<Boolean>() {

                @Override
                public Boolean call() throws TncException, TerminatedException {
                    adapter.solicitRecommendation(connection);
                    return Boolean.TRUE;
                }

            });
        } finally {
            connection.denyMessageReceipt();
        }

    }

}
