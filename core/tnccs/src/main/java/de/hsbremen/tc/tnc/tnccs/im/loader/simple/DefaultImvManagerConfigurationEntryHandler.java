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
package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimv.IMV;

import de.hsbremen.tc.tnc.tnccs.im.loader.ImLoader;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums
.DefaultConfigurationLineClassifierEnum;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;

/**
 * Default configuration entry handler to handle changes of configuration
 * entries to load an IMV.
 *
 *
 */
public class DefaultImvManagerConfigurationEntryHandler extends
        AbstractImManagerConfigurationEntryHandler<IMV> {

    private static final Set<ConfigurationLineClassifier> CLASSIFIER =
            new HashSet<>(Arrays.asList(new ConfigurationLineClassifier[] {
                    DefaultConfigurationLineClassifierEnum.JAVA_IMV}));

    /**
     * Creates the configuration entry handler with the given IMV
     * loader to load an IMV using a configuration entry and the given
     * manager to manage the IMV's life cycle.
     * @param imvLoader the IMV loader
     * @param manager the IMV manager
     */
    public DefaultImvManagerConfigurationEntryHandler(
            final ImLoader<IMV> imvLoader, final ImManager<IMV> manager) {
        super(imvLoader, manager);
    }

    @Override
    public Set<ConfigurationLineClassifier> getSupportedConfigurationLines() {
        return Collections.unmodifiableSet(CLASSIFIER);
    }

}
