package de.hsbremen.tc.tnc.im.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.AttributeSupport;
import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.exception.AttributeException;
import de.hsbremen.tc.tnc.im.exception.AllImIdsAssignedException;
import de.hsbremen.tc.tnc.im.exception.ImModuleNotFoundException;
import de.hsbremen.tc.tnc.im.exception.ImModulesAlreadyLoadedException;
import de.hsbremen.tc.tnc.im.loader.ImFileLoader;
import de.hsbremen.tc.tnc.im.loader.ImLoadParameter;
import de.hsbremen.tc.tnc.im.module.ImModule;
import de.hsbremen.tc.tnc.im.module.ImModuleBuilder;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

// TODO make this more dynamic int the future to load and unload IMC/Vs at will
public class DefaultImcManager implements ImModuleManager<IMC> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImcManager.class);
	
	private long imIds = 0;

	private final TNCC tncc;
	private final ImFileLoader<IMC> loader;
	private ImModuleBuilder<IMC> moduleBuilder;
	
	private final Map<IMC,ImModule<IMC>> imcs;
	
	public DefaultImcManager(TNCC tncc, ImFileLoader<IMC> loader, ImModuleBuilder<IMC> moduleBuilder){
		this.tncc = tncc;
		this.loader = loader;
		this.moduleBuilder = moduleBuilder;
		this.imcs = new LinkedHashMap<>();
	}
	
	@Override
	public void loadImModules(List<ImLoadParameter> imParameterSets) throws ImModulesAlreadyLoadedException{
		if(this.imcs.size() >= 0){
			throw new ImModulesAlreadyLoadedException("Modules are already loaded and cannot be loaded twice.");
		}
		List<IMC> imList = this.loader.loadImlist(); 
		try{
			for (Iterator<IMC> iter = imList.iterator(); iter.hasNext();) {
				IMC imc = iter.next();
				long primaryImcId = this.reserveImcId();
				
				try {
					
					imc.initialize(tncc);
					ImModule<IMC> module = moduleBuilder.createImModule(primaryImcId, imc);
					
					// IMPORTANT this is special to this implementation and not fully standard conform
					// the implementation assumes, that the IMC contains the attributes SupportsTncsFirst and PrimaryImcId 
					// and tries to set them after initialization, this is closer to the behavior of the c-interfaces and
					// currently easier to handle, because the set and get methods of AttributeSupport do not support 
					// the reference to an IMC or any other object, where the attribute should be set. 
					this.lookUpTncsFirstSupport(module);
					this.informImcAboutPrimaryId(module);
					
					this.imcs.put(imc,module);
					
				} catch (TNCException e) {
					long resultCode = e.getResultCode();
					if(resultCode != TNCException.TNC_RESULT_ALREADY_INITIALIZED){
						LOGGER.error("IMC cannot be initialized, because an exception occured.\nIMC will be added to manager and not used.\n",e);
					}
				}
				
				
			}
		}catch(AllImIdsAssignedException e){
			LOGGER.warn("All IMC IDs are assigned, all other IMCs will be ignored and not used.\n",e);
		}
	}

	private void informImcAboutPrimaryId(ImModule<IMC> module) {
		IMC imc = module.getIm();
		if (imc instanceof AttributeSupport) {
			AttributeSupport imcAttr = (AttributeSupport) imc;
			try{
				Long l = new Long(module.getPrimaryId());
				imcAttr.setAttribute(AttributeSupport.TNC_ATTRIBUTEID_PRIMARY_IMC_ID,l);
			}catch(TNCException e){
				LOGGER.warn("Attribute could not actively beeing set for IMC.",e);
			}
		}
		
	}

	private void lookUpTncsFirstSupport(ImModule<IMC> module) {
		IMC imc = module.getIm();
		if (imc instanceof AttributeSupport) {
			AttributeSupport imcAttr = (AttributeSupport) imc;
			try{
				Object o = imcAttr.getAttribute(AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1);
				module.setAttribute(AttributeSupport.TNC_ATTRIBUTEID_IMC_SPTS_TNCS1, o);
			}catch(TNCException e){
				LOGGER.warn("Attribute was not accessible.",e);
			}catch(AttributeException e){
				LOGGER.warn("Attribute cannot be set and will be ignored.", e);
			}
		}
		
	}

	@Override
	public Collection<ImModule<IMC>> getAll() {
		return Collections.unmodifiableCollection(this.imcs.values());
	}

	@Override
	public ImModule<IMC> findById(long id) throws ImModuleNotFoundException {
		for (ImModule<IMC> imcM: this.imcs.values()) {
			if(imcM.getAllImIds().contains(new Long(id))){
				return imcM;
			}
		}
		throw new ImModuleNotFoundException("No ImModule with ID " + id +" was found.");
	}
	
	@Override
	public ImModule<IMC> findByObject(IMC searched) throws ImModuleNotFoundException {
		if(this.imcs.containsKey(searched)){
			return this.imcs.get(searched);
		}
		throw new ImModuleNotFoundException("No ImModule with Object " + searched.toString() +" was found.");
	}

	@Override
	public void reportMessageTypes(ImModule<IMC> module, List<SupportedMessageType> types) {
		if(module != null){
			module.setSupportedMessageTypes(types);
		} // else ignore
	}

	@Override
	public long reserverImId(ImModule<IMC> module) throws AllImIdsAssignedException{
		
		if(module != null){
			long nextImId = reserveImcId();
			module.addImId(nextImId);
			return nextImId;
		}else{
			throw new NullPointerException("ImModule cannot be null.");
		}
	}
	
	private synchronized long reserveImcId() throws AllImIdsAssignedException{
		if(this.imIds < TNCConstants.TNC_IMCID_ANY){
			return ++imIds;
		}else{
			throw new AllImIdsAssignedException("All possible IMC IDs are assigned.");
		}
	}

}
