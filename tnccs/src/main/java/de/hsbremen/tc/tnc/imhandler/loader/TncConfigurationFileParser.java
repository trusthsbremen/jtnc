package de.hsbremen.tc.tnc.imhandler.loader;

import java.io.File;
import java.util.Set;

import de.hsbremen.tc.tnc.imhandler.loader.enums.ConfigurationLineClassifier;

public interface TncConfigurationFileParser {

	public abstract Set<ConfigurationEntry> parseConfigurationEntries(
			File configFile, ConfigurationLineClassifier classifier);

}