package de.hsbremen.tc.tnc.newp.loader;

import java.io.File;

public interface FileChangeListener {

	public abstract void notifyChange(File config);

	public abstract void notifyDelete(File config);

}
