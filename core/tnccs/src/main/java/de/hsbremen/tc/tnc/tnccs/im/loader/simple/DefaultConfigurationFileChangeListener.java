/**
 * The BSD 3-Clause License ("BSD New" or "BSD Simplified")
 *
 * Copyright © 2015 Trust HS Bremen and its Contributors. All rights   
 * reserved.
 *
 * See the CONTRIBUTORS file distributed with this work for additional
 * information regarding copyright ownership.
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

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntryDispatcher;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntryHandler;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileParser;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;

/**
 * Default file change listener to listen for file change events
 * of one file. If an event occurs the file will be parsed for
 * configuration entries and the resulting entries are dispatched
 * to registered configuration handlers.
 *
 *
 */
public class DefaultConfigurationFileChangeListener implements
        ConfigurationFileChangeListener, ConfigurationEntryDispatcher {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DefaultConfigurationFileChangeListener.class);

    private final Map<ConfigurationLineClassifier, Set<ConfigurationEntry>>
    configurationEntries;
    private final ConfigurationFileParser parser;
    private final Map<ConfigurationLineClassifier,
    Set<ConfigurationEntryHandler>> listeners;

    /**
     * Creates the default listener with the given parser. The listener
     * can only handle entries, that are supported by the parser.
     *
     * @param parser the configuration entry parser.
     */
    public DefaultConfigurationFileChangeListener(
            final ConfigurationFileParser parser) {

        // may be accessed from more than one thread
        this.listeners = new ConcurrentHashMap<>();
        this.configurationEntries = new HashMap<>();
        this.parser = parser;
        
        for (ConfigurationLineClassifier classifier : this.parser
                .getSupportedConfigurationLines()) {
            if (!this.configurationEntries.containsKey(classifier)) {
                this.configurationEntries.put(classifier,
                        new HashSet<ConfigurationEntry>());
                this.listeners.put(classifier,
                        new HashSet<ConfigurationEntryHandler>());
            }
        }
    }

    @Override
    public void addHandler(final Set<ConfigurationLineClassifier> classifiers,
            final ConfigurationEntryHandler handler) {
        if (classifiers != null && handler != null) {
            for (ConfigurationLineClassifier classifier : classifiers) {
                if (this.listeners.containsKey(classifier)) {
                    this.listeners.get(classifier).add(handler);
                } else {
                    LOGGER.warn("Classifier "
                            + classifier.linePrefix()
                            + " not supported."
                            + " Handler will not be added for the classifier.");
                }
            }
        }

    }

    @Override
    public void removeHandler(
            final Set<ConfigurationLineClassifier> classifiers,
            final ConfigurationEntryHandler handler) {
        if (classifiers != null && handler != null) {
            for (ConfigurationLineClassifier classifier : classifiers) {
                if (this.listeners.containsKey(classifier)) {
                    this.listeners.get(classifier).remove(handler);
                }
            }
        }
    }

    @Override
    public void notifyChange(final File config) {
        Map<ConfigurationLineClassifier, Set<ConfigurationEntry>> newLines =
                this.parser.parseConfigurationEntries(config);

        if (newLines == null) {
            this.notifyDelete();
        } else if (!configurationEntries.equals(newLines)) {

            // remove old ones
            Set<ConfigurationLineClassifier> removeable = new HashSet<>(
                    this.configurationEntries.keySet());
            removeable.removeAll(newLines.keySet());

            for (ConfigurationLineClassifier classifier : removeable) {
                LOGGER.debug("No entries for classifier "
                        + classifier.linePrefix()
                        + " found. Existing entries will be removed.");
                this.notifyChange(classifier,
                        new HashSet<ConfigurationEntry>());
            }

            // update existing/add new ones
            for (ConfigurationLineClassifier classifier : newLines.keySet()) {

                if (configurationEntries.containsKey(classifier)) {

                    Set<ConfigurationEntry> newEntries = (newLines
                            .get(classifier) != null) ? newLines
                            .get(classifier)
                            : new HashSet<ConfigurationEntry>();

                    if (!this.configurationEntries.get(classifier).equals(
                            newEntries)) {
                        LOGGER.debug("New entries for classifier "
                                + classifier.linePrefix()
                                + " found. Existing entries will be updated.");
                        this.notifyChange(classifier, newEntries);
                    }
                } else {
                    LOGGER.debug("Classifier " + classifier.linePrefix()
                            + " not supported. Entries will be ignored.");
                }
            }
        }
    }

    @Override
    public void notifyDelete() {
        for (ConfigurationLineClassifier classifier : this.configurationEntries
                .keySet()) {
            Set<ConfigurationEntryHandler> handlers = this.listeners
                    .get(classifier);
            for (ConfigurationEntryHandler handler : handlers) {
                handler.notifyDelete();
            }
            this.configurationEntries.get(classifier).clear();
        }
    }

    /**
     * Notifies the handler about a change within the configuration entries
     * identified by the given classifier.
     * @param classifier the identifying line classifier
     * @param entries the configuration entries
     */
    private void notifyChange(final ConfigurationLineClassifier classifier,
            final Set<ConfigurationEntry> entries) {

        this.configurationEntries.put(classifier, entries);

        for (ConfigurationEntryHandler handler
                : this.listeners.get(classifier)) {
            handler.notifyChange(classifier,
                    this.configurationEntries.get(classifier));
        }
    }

}
