package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.util.List;
import java.util.Set;

import de.hsbremen.tc.tnc.tnccs.im.loader.exception.LoadingException;

public interface ImLoader<T> {

	public abstract List<T> loadIms(List<ConfigurationEntry> configs);
	
	public abstract T loadIm(ConfigurationEntry config) throws LoadingException;
	
	public abstract void cleanUp(Set<ConfigurationEntry> currentConfigs);
}
