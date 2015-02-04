package de.hsbremen.tc.tnc.im.adapter.tncs;

import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimv.IMV;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.TNCS;
import org.trustedcomputinggroup.tnc.ifimv.TNCSLong;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

class TncsAdapterIetf implements TncsAdapter, GlobalHandshakeRetryListener {
	
	private final IMV imv;
	private final TNCS tncs;

	TncsAdapterIetf(IMV imv, TNCS tncs) {
		this.imv = imv;
		this.tncs = tncs;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.imc.TnccAdapter#reportMessageTypes(java.util.Set)
	 */
	@Override
	public void reportMessageTypes(Set<SupportedMessageType>supportedTypes)
			throws TncException {
		try{
			if(this.tncs instanceof TNCSLong){
				long[][] messageTypes = SupportedMessageTypeFactory.createSupportedMessageTypeArray(supportedTypes);
				((TNCSLong)this.tncs).reportMessageTypesLong(this.imv, messageTypes[0], messageTypes[1]);
			
			}else{
				
				long[] messageTypes = SupportedMessageTypeFactory.createSupportedMessageTypeArrayLegacy(supportedTypes);
				this.tncs.reportMessageTypes(this.imv, messageTypes);
			}
		}catch(TNCException e){
			throw new TncException(e);
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.imc.TnccAdapter#requestHandshakeRetry(long)
	 */
	@Override
	public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException {
		if(reason.toString().contains("IMC")){
			try{
				this.tncs.requestHandshakeRetry(this.imv, reason.id());
			}catch(TNCException e){
				throw new TncException(e);
			}
		}else{
			throw new TncException("Reason is not useable with IMC and TNCS.", TncExceptionCodeEnum.TNC_RESULT_INVALID_PARAMETER);
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.imc.TnccAdapter#reserveAdditionalId()
	 */
	@Override
	public long reserveAdditionalId() throws TncException {
		if(this.tncs instanceof TNCSLong){
			try{
				return ((TNCSLong) this.tncs).reserveAdditionalIMVID(this.imv);
			}catch(TNCException e){
				throw new TncException(e);
			}
		}
	
		throw new UnsupportedOperationException(this.tncs.getClass().getName() + " instance is not of type " + TNCSLong.class.getSimpleName() + ".");
	}

	@Override
	public GlobalHandshakeRetryListener getHandshakeRetryListener() {
		return this;
	}
	
//	public Object getAttribute(long attributeID) throws TNCException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public void setAttribute(long attributeID, Object attributeValue)
//			throws TNCException {
//		// TODO Auto-generated method stub
//		
//	}

}
