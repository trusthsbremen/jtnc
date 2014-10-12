package de.hsbremen.tc.tnc.im.manager;

import java.util.Collection;
import java.util.List;

import de.hsbremen.tc.tnc.im.exception.AllImIdsAssignedException;
import de.hsbremen.tc.tnc.im.exception.ImModuleNotFoundException;
import de.hsbremen.tc.tnc.im.exception.ImModulesAlreadyLoadedException;
import de.hsbremen.tc.tnc.im.loader.ImLoadParameter;
import de.hsbremen.tc.tnc.im.module.ImModule;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

public interface ImModuleManager<T> {
	
    public void loadImModules(List<ImLoadParameter> imParameterSets) throws ImModulesAlreadyLoadedException;
	
	public Collection<ImModule<T>> getAll();
	
	public ImModule<T> findById(long id) throws ImModuleNotFoundException;
	
	public ImModule<T> findByObject(T searched) throws ImModuleNotFoundException;
	
	public void reportMessageTypes(ImModule<T> module, List<SupportedMessageType> types);
	
	public long reserverImId(ImModule<T> module) throws AllImIdsAssignedException;

	
}
