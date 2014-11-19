package de.hsbremen.tc.tnc.imhandler.loader;

import java.util.List;

public interface ImFileLoader<T> {
	
	public abstract List<T> loadImlist();
}
