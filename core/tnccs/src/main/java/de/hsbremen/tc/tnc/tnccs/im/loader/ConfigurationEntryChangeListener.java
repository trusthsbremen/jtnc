package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.util.Set;

import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;

public interface ConfigurationEntryChangeListener {

    public abstract Set<ConfigurationLineClassifier> getSupportedConfigurationLines();

    public abstract void notifyDelete();

    public abstract void notifyChange(Set<ConfigurationEntry> entries,
            ConfigurationLineClassifier classfier);

}
