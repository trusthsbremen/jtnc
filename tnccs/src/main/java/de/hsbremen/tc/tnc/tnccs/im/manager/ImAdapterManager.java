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
package de.hsbremen.tc.tnc.tnccs.im.manager;

import java.util.Map;

import de.hsbremen.tc.tnc.tnccs.adapter.im.ImAdapter;
import de.hsbremen.tc.tnc.tnccs.im.route.ImMessageRouter;

/**
 * Generic base to manage the IM(C/V) adapter life cycle.
 *
 * @author Carl-Heinz Genzel
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
     * Removes an IM(C/V) adapter of the IM(C/V)
     * with the given primary ID.
     *
     * @param id the primary ID
     */
    void removeAdapter(long id);
}
