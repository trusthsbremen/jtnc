package org.ietf.nea.pb.validate;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoNoSkip;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageAccessRecommendationValidator implements TnccsValidator<PbMessage>{

	private static final class Singleton{
		private static final PbMessageAccessRecommendationValidator INSTANCE = new PbMessageAccessRecommendationValidator();  
	}
	
	public static PbMessageAccessRecommendationValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageAccessRecommendationValidator() {
    	// Singleton
    }
	
	@Override
	public void validate(PbMessage message) throws ValidationException {
		if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType()){
    		throw new IllegalArgumentException("Unsuspected message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
		
		long fixedMessageLength = PbMessage.FIXED_LENGTH + PbMessageValueAccessRecommendation.FIXED_LENGTH;
		
		NoNoSkip.check(message.getFlags());
		MinMessageLength.check(message.getLength(), fixedMessageLength);
	}
	
	@Override
	public void addValidator(Long vendorId, Long messageType,
			TnccsValidator<PbMessage> validator) {
		throw new UnsupportedOperationException("Method is not supported by this implementation.");
	}

	@Override
	public void removeValidator(Long vendorId, Long messageType) {
		throw new UnsupportedOperationException("Method is not supported by this implementation.");
	}
}
