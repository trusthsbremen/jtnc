package de.hsbremen.tc.tnc.adapter.tncc;

import java.util.HashSet;
import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.newp.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.newp.manager.ImManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

class TncsAdapterIetf implements TNCS{

	private final ImManager<IMV> moduleManager;
	private final GlobalHandshakeRetryListener listener;
	private final Attributed attributes;
	
	TncsAdapterIetf(final ImManager<IMV> moduleManager, final Attributed attributes, final GlobalHandshakeRetryListener listener) {
		this.moduleManager = moduleManager;
		this.attributes = attributes;
		this.listener = listener;
	}

	protected ImManager<IMV> getManager(){
		return this.moduleManager;
	}
	
	@Override
	public void reportMessageTypes(final IMV imv, final long[] supportedTypes)
			throws TNCException {

		Set<SupportedMessageType> sTypes = new HashSet<>();
		
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
		
		try {
			this.moduleManager.reportSupportedMessagesTypes(imv, sTypes);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
	}

	@Override
	public void requestHandshakeRetry(final IMV imv, final long reason) throws TNCException {
		// TODO is the IMC needed as parameter?
		try {
			this.listener.requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum.fromCode(reason));
		} catch (TncException e) {
			throw new TNCException(e.getMessage(),e.getResultCode().result());
		}
	
	}

	@Override
	public Object getAttribute(long attributeID) throws TNCException {
		try {
			return this.attributes.getAttribute(DefaultTncAttributeTypeFactory.getInstance().fromId(attributeID));
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
	}

	@Override
	public void setAttribute(long attributeID, Object attributeValue)
			throws TNCException {
		try {
			this.attributes.setAttribute(DefaultTncAttributeTypeFactory.getInstance().fromId(attributeID), attributeValue);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
	}
	
	
}
