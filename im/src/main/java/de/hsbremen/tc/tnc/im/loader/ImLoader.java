package de.hsbremen.tc.tnc.im.loader;

import java.util.List;

public interface ImLoader<T> {
	
	public abstract List<T> loadImlist();
}
