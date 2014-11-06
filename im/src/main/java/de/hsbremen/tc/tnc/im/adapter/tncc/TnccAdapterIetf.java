package de.hsbremen.tc.tnc.im.adapter.tncc;

import java.util.Set;

import org.trustedcomputinggroup.tnc.ifimc.IMC;
import org.trustedcomputinggroup.tnc.ifimc.TNCC;
import org.trustedcomputinggroup.tnc.ifimc.TNCCLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;
import de.hsbremen.tc.tnc.im.adapter.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;
import de.hsbremen.tc.tnc.im.module.SupportedMessageTypeFactory;

class TnccAdapterIetf implements TnccAdapter, GlobalHandshakeRetryListener {
	
	private final IMC imc;
	private final TNCC tncc;

	TnccAdapterIetf(IMC imc, TNCC tncc) {
		this.imc = imc;
		this.tncc = tncc;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.imc.TnccAdapter#reportMessageTypes(java.util.Set)
	 */
	@Override
	public void reportMessageTypes(Set<SupportedMessageType>supportedTypes)
			throws TNCException {
		if(this.tncc instanceof TNCCLong){
			long[][] messageTypes = SupportedMessageTypeFactory.createSupportedMessageTypeArray(supportedTypes);
			((TNCCLong)this.tncc).reportMessageTypesLong(this.imc, messageTypes[0], messageTypes[1]);
		
		}else{
			
			long[] messageTypes = SupportedMessageTypeFactory.createSupportedMessageTypeArrayLegacy(supportedTypes);
			this.tncc.reportMessageTypes(this.imc, messageTypes);
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.imc.TnccAdapter#requestHandshakeRetry(long)
	 */
	@Override
	public void requestGlobalHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TNCException {
		if(reason.toString().contains("IMC")){
			this.tncc.requestHandshakeRetry(this.imc, reason.code());
		}else{
			throw new TNCException("Reason is not useable with IMC and TNCC.", TNCException.TNC_RESULT_INVALID_PARAMETER);
		}
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.adapter.imc.TnccAdapter#reserveAdditionalId()
	 */
	@Override
	public long reserveAdditionalId() throws TNCException {
		if(this.tncc instanceof TNCCLong){
			return ((TNCCLong) this.tncc).reserveAdditionalIMCID(this.imc);
		}
	
		throw new UnsupportedOperationException(this.tncc.getClass().getName() + " instance is not of type " + TNCCLong.class.getSimpleName() + ".");
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
