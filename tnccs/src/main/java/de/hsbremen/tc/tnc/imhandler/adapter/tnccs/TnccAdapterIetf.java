package de.hsbremen.tc.tnc.imhandler.adapter.tnccs;

import java.util.LinkedList;
import java.util.List;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.imhandler.exception.ImModuleNotFoundException;
import de.hsbremen.tc.tnc.imhandler.manager.ImModuleManager;
import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

class TnccAdapterIetf implements TNCC{

	private final ImModuleManager<IMC> moduleManager;
	
	TnccAdapterIetf(final ImModuleManager<IMC> moduleManager) {
		this.moduleManager = moduleManager;
	}

	@Override
	public void reportMessageTypes(final IMC imc, final long[] supportedTypes)
			throws TNCException {
		
		TnccsImModuleHolder<IMC> im = this.findModule(imc);
		
		List<SupportedMessageType> sTypes = new LinkedList<>();
		
		if(supportedTypes != null){
			for (long l : supportedTypes) {
				try{
					SupportedMessageType mType = SupportedMessageTypeFactory.createSupportedMessageTypeLegacy(l);
					sTypes.add(mType);
				}catch (IllegalArgumentException e){
					throw new TNCException(e.getMessage(), TNCException.TNC_RESULT_INVALID_PARAMETER);
				}
			}
		}
		
		this.moduleManager.reportMessageTypes(im, sTypes);
		
	}

	@Override
	public void requestHandshakeRetry(final IMC imc, final long reason) throws TNCException {
		TnccsImModuleHolder<IMC> im = this.findModule(imc);
		
		// TODO where to direct the handshake retry
		

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
