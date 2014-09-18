package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoSkip;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageAssessmentResultValidator implements TnccsValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageAssessmentResultValidator INSTANCE = new PbMessageAssessmentResultValidator();  
	}
	public static PbMessageAssessmentResultValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageAssessmentResultValidator() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
    
		long fixedMessageLength = PbMessage.FIXED_LENGTH + PbMessageValueAssessmentResult.FIXED_LENGTH;
		
		NoSkip.check(message.getFlags());
		MinMessageLength.check(message.getLength(), fixedMessageLength);
        // checkResultCode not needed because there is a fixed value enum as member variable.
    }
}
