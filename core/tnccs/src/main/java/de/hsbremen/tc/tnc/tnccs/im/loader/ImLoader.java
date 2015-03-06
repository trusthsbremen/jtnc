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
package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.util.List;
import java.util.Set;

import de.hsbremen.tc.tnc.tnccs.im.loader.exception.LoadingException;

/**
 * Generic loader to load IM(C/V) according to a configuration entry
 * and handle the class path for the IM(C/V).
 *
 *
 * @param <T> the loadable type (e.g. IMC or IMV)
 */
public interface ImLoader<T> {

    /**
     * Loads a list of IM(C/V) according to the given configuration entries.
     *
     * @param configs the configuration entries
     * @return a list of IM(C/V)
     */
    List<T> loadIms(final List<ConfigurationEntry> configs);

    /**
     * Loads an IM(C/V) according to the given configuration entry.
     * @param config the configuration entry
     * @return an IM(C/V)
     * @throws LoadingException if loading fails
     */
    T loadIm(final ConfigurationEntry config) throws LoadingException;

    /**
     * Enables the loader to check the currently valid configuration
     * entries to adapt its internal class paths to the current
     * configuration entries.
     *
     * @param currentConfigs the current configuration entries
     */
    void cleanUp(final Set<ConfigurationEntry> currentConfigs);
}
