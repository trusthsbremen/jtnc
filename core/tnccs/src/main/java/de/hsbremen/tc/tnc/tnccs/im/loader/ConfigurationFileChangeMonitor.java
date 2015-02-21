package de.hsbremen.tc.tnc.tnccs.im.loader;

public interface ConfigurationFileChangeMonitor{

	public abstract void add(ConfigurationFileChangeHandler observer);

	public abstract void remove(ConfigurationFileChangeHandler observer);

	public abstract void start();
	public abstract void stop();
}