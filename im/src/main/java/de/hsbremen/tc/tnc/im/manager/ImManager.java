package de.hsbremen.tc.tnc.im.manager;

import java.io.File;
import java.util.List;

import de.hsbremen.tc.tnc.im.container.ImContainer;
import de.hsbremen.tc.tnc.im.container.SupportedMessageType;

public interface ImManager<T> {

	public void loadAll(File tncConfig);
	
	public List<ImContainer<T>> getAll();
	
	public ImContainer<T> get(T searched);
	
	public void reportMessageTypes(T searched, List<SupportedMessageType> types);
	
	public void reserverImcId(T t);
}
