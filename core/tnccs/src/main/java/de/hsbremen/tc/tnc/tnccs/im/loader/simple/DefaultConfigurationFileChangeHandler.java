package de.hsbremen.tc.tnc.tnccs.im.loader.simple;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntryChangeListener;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationEntry;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileChangeHandler;
import de.hsbremen.tc.tnc.tnccs.im.loader.ConfigurationFileParser;
import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;

public class DefaultConfigurationFileChangeHandler implements ConfigurationFileChangeHandler {

    private Map<ConfigurationLineClassifier, Set<ConfigurationEntry>> configurationEntries;
    private final ConfigurationFileParser parser;
    private final Map<ConfigurationEntryChangeListener, Set<ConfigurationLineClassifier>> listeners;

    public DefaultConfigurationFileChangeHandler(
            ConfigurationFileParser fileFilter,
            Map<ConfigurationEntryChangeListener, Set<ConfigurationLineClassifier>> listeners) {
        this.configurationEntries = new HashMap<>();
        this.listeners = listeners;
        for (Entry<ConfigurationEntryChangeListener, Set<ConfigurationLineClassifier>> entry : this.listeners
                .entrySet()) {
            for (ConfigurationLineClassifier classifier : entry.getValue()) {
                if (!this.configurationEntries.containsKey(classifier)) {
                    this.configurationEntries.put(classifier,
                            new HashSet<ConfigurationEntry>());
                }
            }
        }
        this.parser = fileFilter;
    }

    @Override
    public void notifyChange(File config) {
        for (Entry<ConfigurationLineClassifier, Set<ConfigurationEntry>> entry : this.configurationEntries
                .entrySet()) {

            Set<ConfigurationEntry> oldLines = entry.getValue();
            Set<ConfigurationEntry> newLines = this.parser
                    .parseConfigurationEntries(config, entry.getKey());

            if (newLines != null && !oldLines.equals(newLines)) {

                this.configurationEntries.put(entry.getKey(), newLines);
                this.notifyChange(newLines, entry.getKey());
            }
        }
    }

    @Override
    public void notifyDelete(File config) {
        for (Set<ConfigurationEntry> entries : this.configurationEntries
                .values()) {
            entries.clear();
        }
        this.notifyDelete();
    }

    private void notifyDelete() {
        for (ConfigurationEntryChangeListener listener : this.listeners.keySet()) {
            listener.notifyDelete();
        }
    }

    private void notifyChange(Set<ConfigurationEntry> entries,
            ConfigurationLineClassifier classifier) {
        for (Entry<ConfigurationEntryChangeListener, Set<ConfigurationLineClassifier>> entry : this.listeners
                .entrySet()) {
            if (entry.getValue().contains(classifier)) {
                entry.getKey().notifyChange(entries, classifier);
            }
        }
    }

}
