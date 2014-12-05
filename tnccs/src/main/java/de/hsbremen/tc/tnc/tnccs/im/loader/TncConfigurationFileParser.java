package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;
import java.util.Set;

import de.hsbremen.tc.tnc.tnccs.im.loader.enums.ConfigurationLineClassifier;

public interface TncConfigurationFileParser {

	public abstract Set<ConfigurationEntry> parseConfigurationEntries(
			File configFile, ConfigurationLineClassifier classifier);

}