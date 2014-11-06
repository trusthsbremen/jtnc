package de.hsbremen.tc.tnc.imhandler.adapter.tnccs;

import java.util.LinkedList;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.module.SupportedMessageType;
import de.hsbremen.tc.tnc.im.module.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.imhandler.exception.AllImIdsAssignedException;
import de.hsbremen.tc.tnc.imhandler.exception.ImModuleNotFoundException;
import de.hsbremen.tc.tnc.imhandler.manager.ImModuleManager;
import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;

class TnccAdapterIetfLong implements TNCC, TNCCLong {

	private final ImModuleManager<IMC> moduleManager;
	private final TNCC stdTncc;
	
	TnccAdapterIetfLong(final TNCC stdTncc, final ImModuleManager<IMC> moduleManager) {
		this.moduleManager = moduleManager;
		this.stdTncc = stdTncc;
	}

	/* STD */
	@Override
	public void reportMessageTypes(IMC imc, long[] supportedTypes)
			throws TNCException {
		this.stdTncc.reportMessageTypes(imc, supportedTypes);
		
	}

	@Override
	public void requestHandshakeRetry(IMC imc, long reason) throws TNCException {
		this.stdTncc.requestHandshakeRetry(imc, reason);
	}

	/* LONG */
	@Override
	public void reportMessageTypesLong(IMC imc, long[] supportedVendorIDs,
			long[] supportedSubtypes) throws TNCException {
		
		TnccsImModuleHolder<IMC> im = this.findModule(imc);
		
		List<SupportedMessageType> sTypes = new LinkedList<>();
		
		if(supportedVendorIDs != null && supportedSubtypes != null){
			
			if(supportedVendorIDs.length != supportedSubtypes.length){
				throw new TNCException("The supplied arrays have a different length.", TNCException.TNC_RESULT_INVALID_PARAMETER);
			}
			
			for (int i = 0; i < supportedVendorIDs.length; i++) {
				try{
					SupportedMessageType mType = SupportedMessageTypeFactory.createSupportedMessageType(supportedVendorIDs[i], supportedSubtypes[i]);
					sTypes.add(mType);
				}catch (IllegalArgumentException e){
					throw new TNCException(e.getMessage(), TNCException.TNC_RESULT_INVALID_PARAMETER);
				}
			}
		}
		
		this.moduleManager.reportMessageTypes(im, sTypes);
		
	}

	@Override
	public long reserveAdditionalIMCID(IMC imc) throws TNCException {
		TnccsImModuleHolder<IMC> im = this.findModule(imc);

		try {
			long newImId = this.moduleManager.reserverImId(im);
			return newImId;
		} catch (AllImIdsAssignedException | NullPointerException e) {
			throw new TNCException(e.getMessage(),TNCException.TNC_RESULT_OTHER);
		}

	}
	
	private TnccsImModuleHolder<IMC> findModule(IMC imc) throws TNCException{
		TnccsImModuleHolder<IMC> im;
		
		try {
			im = this.moduleManager.findByObject(imc);
		} catch (ImModuleNotFoundException e) {
			throw new TNCException("IMC module not found.", TNCException.TNC_RESULT_INVALID_PARAMETER);
		}
		
		return im;
	}
}
