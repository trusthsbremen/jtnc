package de.hsbremen.tc.tnc.newp.loader;

import java.util.Set;

import de.hsbremen.tc.tnc.newp.loader.enums.ConfigurationLineClassifier;

public interface ConfigurationChangeListener {

	public abstract void notifyDelete();

	public abstract void notifyChange(Set<ConfigurationEntry> entries, ConfigurationLineClassifier classfier);

}
