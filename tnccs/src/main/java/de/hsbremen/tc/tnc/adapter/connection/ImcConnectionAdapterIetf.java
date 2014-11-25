package de.hsbremen.tc.tnc.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.attribute.TncClientAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.session.context.SessionConnectionContext;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class ImcConnectionAdapterIetf implements ImcConnectionAdapter{

	private boolean receiving;
	private final int primaryImcId;
	private final SessionConnectionContext session;

	public ImcConnectionAdapterIetf(int primaryImcId,
			SessionConnectionContext session) {
		this.primaryImcId = primaryImcId;
		this.session = session;
		this.receiving = false;
	}

	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.connection.ImConnectionAdapter#getId()
	 */
	@Override
	public long getImId() {
		return this.primaryImcId;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.connection.ImConnectionAdapter#allowSending()
	 */
	@Override
	public void allowMessageReceipt(){
		this.receiving = true;	
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.connection.ImConnectionAdapter#denySending()
	 */
	@Override
	public void denyMessageReceipt(){
		this.receiving = false;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.adapter.connection.ImConnectionAdapter#isSending()
	 */
	@Override
	public boolean isReceiving(){
		return this.receiving;
	}

	@Override
	public void sendMessage(long messageType, byte[] message)
			throws TNCException {
	
		if(!this.isReceiving()){
			throw new TNCException("Connection is currently not allowed to receive messages.", TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION.result());
		}
		
		long vendorId = messageType >>> 8;
		long type = messageType & 0xFF;
		
		TnccsMessage m = null;
		try {
			m = PbMessageFactoryIetf.createIm(new PbMessageImFlagsEnum[0], vendorId, type, this.primaryImcId, (int)TNCConstants.TNC_IMVID_ANY, message);
		} catch (ValidationException e) {
			throw new TNCException(e.getCause().getMessage(), e.getCause().getErrorCode());
		}
		
		try{
			this.sendMessage(m);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
	}

	@Override
	public void requestHandshakeRetry(long reason) throws TNCException {
		try {
			this.session.requestHandshakeRetry(ImHandshakeRetryReasonEnum.fromCode(reason));
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
		
	}

	@Override
	public Object getAttribute(long attributeID) throws TNCException {
		
		if(attributeID == TncClientAttributeTypeEnum.TNC_ATTRIBUTEID_PRIMARY_IMC_ID.id()){
			return new Long(this.primaryImcId);
		}else{
			try {
				return this.session.getAttribute(DefaultTncAttributeTypeFactory.getInstance().fromId(attributeID));
			} catch (TncException e) {
				throw new TNCException(e.getMessage(), e.getResultCode().result());
			}
		}
	}

	@Override
	public void setAttribute(long attributeID, Object attributeValue)
			throws TNCException {
		throw new UnsupportedOperationException("The operation setAttribute(...) is not supported, because there are no attributes to set.");
	}
	
	protected final void sendMessage(TnccsMessage message) throws TncException{
		if(message != null){
			this.session.addMessage(message);
		}
	}
}
