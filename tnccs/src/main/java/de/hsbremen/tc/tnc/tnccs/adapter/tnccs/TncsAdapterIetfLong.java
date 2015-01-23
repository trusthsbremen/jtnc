package de.hsbremen.tc.tnc.tnccs.adapter.tnccs;

import java.util.HashSet;
import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCSLong;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.tnccs.im.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.im.manager.ImManager;

class TncsAdapterIetfLong extends TncsAdapterIetf implements TNCSLong{

	TncsAdapterIetfLong(final ImManager<IMV> moduleManager, final Attributed attributes, final GlobalHandshakeRetryListener listener) {
		super(moduleManager, attributes, listener);
	}

	@Override
	public void reportMessageTypesLong(IMV imv, long[] supportedVendorIDs,
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
			super.getManager().reportSupportedMessagesTypes(imv, sTypes);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
		
	}

	@Override
	public long reserveAdditionalIMVID(IMV imv) throws TNCException {
		try {
			return super.getManager().reserveAdditionalId(imv);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
	}

}
