package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimv.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncServerAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

class ImvConnectionAdapterIetf extends AbstractImConnectionAdapter implements ImvConnectionAdapter{

	private final ImvConnectionContext session;
	private long maxMessageSize;
	
	ImvConnectionAdapterIetf(int primaryImvId,
			ImvConnectionContext session) {
		super(primaryImvId);
		this.session = session;

		try{
			Object o = session.getAttribute(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_MESSAGE_SIZE);
			if(o instanceof Long){
				this.maxMessageSize = ((Long)o).longValue();
			}
		}catch(TncException | UnsupportedOperationException e){
			this.maxMessageSize = HSBConstants.TCG_IM_MAX_MESSAGE_SIZE_UNKNOWN;
		}
		
	}

	@Override
	public void sendMessage(long messageType, byte[] message)
			throws TNCException {
	
		if(messageType == (TNCConstants.TNC_VENDORID_ANY << 8 | TNCConstants.TNC_SUBTYPE_ANY)){
			throw new TNCException("Message type is set to reserved type.", TNCException.TNC_RESULT_INVALID_PARAMETER);
		}
		
		if(!this.isReceiving()){
			throw new TNCException("Connection is currently not allowed to receive messages.", TNCException.TNC_RESULT_ILLEGAL_OPERATION);
		}
		
		this.checkMessageSize(message.length);
		
		long vendorId = messageType >>> 8;
		long type = messageType & 0xFF;
		
		TnccsMessage m = null;
		try {
			m = PbMessageFactoryIetf.createIm(new PbMessageImFlagsEnum[0], vendorId, type, (int)TNCConstants.TNC_IMCID_ANY,(int) super.getImId(), message);
		} catch (ValidationException e) {
			throw new TNCException(e.getCause().getMessage(), e.getCause().getErrorCode());
		}
		
		try{
			this.sendMessage(m);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().id());
		}
	}

	@Override
	public void requestHandshakeRetry(long reason) throws TNCException {
		try {
			this.session.requestHandshakeRetry(ImHandshakeRetryReasonEnum.fromId(reason));
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().id());
		}
		
	}

	@Override
	public Object getAttribute(long attributeID) throws TNCException {
		
		if(attributeID == TncServerAttributeTypeEnum.TNC_ATTRIBUTEID_PRIMARY_IMV_ID.id()){
			return new Long(super.getImId());
		}else{
			try {
				return this.session.getAttribute(DefaultTncAttributeTypeFactory.getInstance().fromId(attributeID));
			} catch (TncException e) {
				throw new TNCException(e.getMessage(), e.getResultCode().id());
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

	@Override
	public void provideRecommendation(long recommendation, long evaluation)
			throws TNCException {
		if(!this.isReceiving()){
			throw new TNCException("Connection is currently not allowed to receive messages.", TNCException.TNC_RESULT_ILLEGAL_OPERATION);
		}
		
		ImvRecommendationPair recommendationPair = null;
		try{
			recommendationPair = ImvRecommendationPairFactory.createRecommendationPair(recommendation, evaluation);
		}catch(IllegalArgumentException e){
			throw new TNCException(e.getMessage(), TNCException.TNC_RESULT_INVALID_PARAMETER);
		}
		
		try {
			this.session.addRecommendation(super.getImId(),recommendationPair);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().id());
		}
	}
	
	protected void checkMessageSize(int length) throws TNCException {
		if(this.maxMessageSize != HSBConstants.TCG_IM_MAX_MESSAGE_SIZE_UNKNOWN && this.maxMessageSize != HSBConstants.TCG_IM_MAX_MESSAGE_SIZE_UNLIMITED){
			if(length > this.maxMessageSize){
				throw new TNCException("Maximum message size of "+this.maxMessageSize+" exceeded.", TNCException.TNC_RESULT_EXCEEDED_MAX_MESSAGE_SIZE);
			}
		}
	}
}
