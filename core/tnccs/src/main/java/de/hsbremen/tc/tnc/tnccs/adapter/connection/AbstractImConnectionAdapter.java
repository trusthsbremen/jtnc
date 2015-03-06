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
package de.hsbremen.tc.tnc.tnccs.adapter.connection;

/**
 * Generic base for an IM(C/V) connection adapter with connection control
 * functions. Especially important for inheritance.
 *
 *
 */
public class AbstractImConnectionAdapter implements ImConnectionAdapter {

    private boolean receiving;
    private final int primaryId;

    /**
     * Creates an IM(C/V) connection adapter base for the given
     * IM(C/V) ID.
     *
     * @param primaryId the IM(C/V) ID
     */
    protected AbstractImConnectionAdapter(final int primaryId) {
        this.primaryId = primaryId;
        this.receiving = false;
    }

    @Override
    public long getImId() {
        return this.primaryId;
    }

    @Override
    public void allowMessageReceipt() {
        this.receiving = true;
    }

    @Override
    public void denyMessageReceipt() {
        this.receiving = false;
    }

    @Override
    public boolean isReceiving() {
        return this.receiving;
    }
}
