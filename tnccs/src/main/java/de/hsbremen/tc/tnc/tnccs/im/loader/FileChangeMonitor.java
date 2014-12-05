package de.hsbremen.tc.tnc.tnccs.im.loader;

public interface FileChangeMonitor extends Runnable{

	public abstract void add(FileChangeListener observer);

	public abstract void remove(FileChangeListener observer);

}