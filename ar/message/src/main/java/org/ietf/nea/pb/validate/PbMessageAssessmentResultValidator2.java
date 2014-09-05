package org.ietf.nea.pb.validate;

import java.util.Arrays;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsHeaderValidator;

public class PbMessageAssessmentResultValidator2 implements TnccsHeaderValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageAssessmentResultValidator2 INSTANCE = new PbMessageAssessmentResultValidator2();  
	}
	public static PbMessageAssessmentResultValidator2 getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageAssessmentResultValidator2() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
    	PbCommonMessageValidator2.getInstance().validate(message);
    	this.checkNoSkipSet(message);
        this.checkFixedMessageLength(message);
        // checkResultCode not needed because there is a fixed value enum as member variable.
    }
    
	private void checkNoSkipSet(final PbMessage message) throws ValidationException {
        if(!message.getFlags().contains(PbMessageFlagsEnum.NOSKIP)){
        	
            throw new ValidationException("NOSKIP must be set for this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Arrays.toString(message.getFlags().toArray()));
        }
    }
    
    private void checkFixedMessageLength(final PbMessage message) throws ValidationException {
        if(message.getLength() < PbMessageValueAssessmentResult.FIXED_LENGTH + PbMessage.FIXED_LENGTH){
            throw new ValidationException("Message has an invalid length for its type.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(message.getLength()));
        }
    }
}
