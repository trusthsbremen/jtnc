package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.util.List;

import de.hsbremen.tc.tnc.tnccs.im.loader.simple.DefaultConfigurationEntryImJava;

public interface ImLoader<T> {

	public abstract List<T> loadIms(List<DefaultConfigurationEntryImJava> configs);
	
	public abstract T loadIm(DefaultConfigurationEntryImJava config);
}
