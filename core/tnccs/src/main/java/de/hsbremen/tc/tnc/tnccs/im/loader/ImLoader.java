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
