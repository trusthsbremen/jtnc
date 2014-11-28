package de.hsbremen.tc.tnc.adapter.tncc;

import java.util.HashSet;
import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.newp.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.newp.ImManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

class TnccAdapterIetfLong extends TnccAdapterIetf implements TNCCLong{

	TnccAdapterIetfLong(final ImManager<IMC> moduleManager, final Attributed attributes, final GlobalHandshakeRetryListener listener) {
		super(moduleManager, attributes, listener);
	}

	@Override
	public void reportMessageTypesLong(IMC imc, long[] supportedVendorIDs,
			long[] supportedSubtypes) throws TNCException {
		
		Set<SupportedMessageType> sTypes = new HashSet<>();
		
		if(supportedVendorIDs != null && supportedSubtypes != null){
			
			if(supportedVendorIDs.length != supportedSubtypes.length){
				throw new TNCException("The provided arrays have a different length. ("+supportedVendorIDs.length+":"+supportedSubtypes.length+")", TNCException.TNC_RESULT_INVALID_PARAMETER);
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
		
		try {
			super.getManager().reportSupportedMessagesTypes(imc, sTypes);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
		
	}

	@Override
	public long reserveAdditionalIMCID(IMC imc) throws TNCException {
		try {
			return super.getManager().reserveAdditionalId(imc);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
	}

}
