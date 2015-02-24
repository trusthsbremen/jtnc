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
 * @author Carl-Heinz Genzel
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
