package de.hsbremen.tc.tnc.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimv.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;

import de.hsbremen.tc.tnc.attribute.DefaultTncAttributeTypeFactory;
import de.hsbremen.tc.tnc.attribute.TncServerAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class ImvConnectionAdapterIetf extends AbstractImConnectionAdapter implements ImvConnectionAdapter{

	private final ServerSessionConnectionContext session;

	public ImvConnectionAdapterIetf(int primaryImvId,
			ServerSessionConnectionContext session) {
		super(primaryImvId);
		this.session = session;

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
			m = PbMessageFactoryIetf.createIm(new PbMessageImFlagsEnum[0], vendorId, type, (int)TNCConstants.TNC_IMCID_ANY,(int) super.getImId(), message);
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
		
		if(attributeID == TncServerAttributeTypeEnum.TNC_ATTRIBUTEID_PRIMARY_IMV_ID.id()){
			return new Long(super.getImId());
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

	@Override
	public void provideRecommendation(long recommendation, long evaluation)
			throws TNCException {
		if(!this.isReceiving()){
			throw new TNCException("Connection is currently not allowed to receive messages.", TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION.result());
		}
		
		ImvRecommendationPair recommendationPair = null;
		try{
			ImvRecommendationPairFactory.createRecommendationPair(recommendation, evaluation);
		}catch(IllegalArgumentException e){
			throw new TNCException(e.getMessage(), TNCException.TNC_RESULT_INVALID_PARAMETER);
		}
		
		try {
			this.session.addRecommendation(recommendationPair);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
	}
}
