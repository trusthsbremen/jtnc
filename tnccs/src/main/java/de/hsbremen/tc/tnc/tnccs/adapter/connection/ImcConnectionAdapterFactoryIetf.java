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
 * The factory creates an IMC connection adapter according to IETF/TCG
 * specifications.
 *
 * @author Carl-Heinz Genzel
 *
 */
public class ImcConnectionAdapterFactoryIetf implements
        ImcConnectionAdapterFactory {

    private final ImcConnectionContext context;

    /**
     * Creates the factory with the given IMC connection context.
     *
     * @param context the IMC connection context
     */
    public ImcConnectionAdapterFactoryIetf(final ImcConnectionContext context) {
        if (context == null) {
            throw new NullPointerException(
                    "Connection context cannot be NULL.");
        }
        this.context = context;
    }

    @Override
    public ImcConnectionAdapter createConnection(final long primaryId) {
        return new ImcConnectionAdapterIetfLong((int) primaryId, context);
    }

}
