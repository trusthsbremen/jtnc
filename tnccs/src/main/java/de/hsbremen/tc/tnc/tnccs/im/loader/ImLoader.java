package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.util.List;

public interface ImLoader<T> {

	public abstract List<T> loadIms(List<ImConfigurationEntry> configs);
	
	public abstract T loadIm(ImConfigurationEntry config);
}
