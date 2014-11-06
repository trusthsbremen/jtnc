package de.hsbremen.tc.tnc.imhandler.manager;

import java.util.Collection;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.im.module.SupportedMessageType;
import de.hsbremen.tc.tnc.imhandler.exception.AllImIdsAssignedException;
import de.hsbremen.tc.tnc.imhandler.exception.ImModuleNotFoundException;
import de.hsbremen.tc.tnc.imhandler.exception.ImModulesAlreadyLoadedException;
import de.hsbremen.tc.tnc.imhandler.loader.ImLoadParameter;
import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;

public interface ImModuleManager<T> {
	
    public void loadImModules(List<ImLoadParameter> imParameterSets) throws ImModulesAlreadyLoadedException;
	
	public Collection<TnccsImModuleHolder<IMC>> getAll();
	
	public TnccsImModuleHolder<T> findById(long id) throws ImModuleNotFoundException;
	
	public TnccsImModuleHolder<T> findByObject(T searched) throws ImModuleNotFoundException;
	
	public void reportMessageTypes(TnccsImModuleHolder<T> module, List<SupportedMessageType> types);
	
	public long reserverImId(TnccsImModuleHolder<T> module) throws AllImIdsAssignedException;

	
}
