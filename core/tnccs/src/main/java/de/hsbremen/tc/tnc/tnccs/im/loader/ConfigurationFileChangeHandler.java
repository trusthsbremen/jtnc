package de.hsbremen.tc.tnc.tnccs.im.loader;

import java.io.File;

public interface ConfigurationFileChangeHandler {

	public abstract void notifyChange(File config);

	public abstract void notifyDelete(File config);

}
