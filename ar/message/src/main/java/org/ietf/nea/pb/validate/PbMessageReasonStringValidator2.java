package org.ietf.nea.pb.validate;

import java.util.Arrays;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsHeaderValidator;

public class PbMessageReasonStringValidator2 implements TnccsHeaderValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageReasonStringValidator2 INSTANCE = new PbMessageReasonStringValidator2();  
	}
	
	public static PbMessageReasonStringValidator2 getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageReasonStringValidator2() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
        PbCommonMessageValidator2.getInstance().validate(message);
        this.checkNoSkipNotSet(message);
        this.checkFixedMessageLength(message);
        PbMessageValueReasonString value = (PbMessageValueReasonString) message.getValue();
        // order important - first check zero than null-termination
        this.checkForZeroString(value);
        this.checkReasonStringForNullTermination(value);
        this.checkLanguageCodeForNullTermination(value);
    }
    
    private void checkNoSkipNotSet(final PbMessage message) throws ValidationException {
        if(message.getFlags().contains(PbMessageFlagsEnum.NOSKIP)){
            throw new ValidationException("NOSKIP not allowed in this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Arrays.toString(message.getFlags().toArray()));
        }
    }
    
    private void checkFixedMessageLength(final PbMessage message) throws ValidationException {
        if(message.getLength() != PbMessageValueReasonString.FIXED_LENGTH + PbMessage.FIXED_LENGTH){
            throw new ValidationException("Message has an invalid length for its type.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(message.getLength()));
        }
    }
    
    private void checkForZeroString(final PbMessageValueReasonString value) throws ValidationException{
    	if(value == null || value.getReasonString() == ""){
            throw new ValidationException("Message contains zero-string.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),value.getReasonString());
        }
    }
    
    private void checkReasonStringForNullTermination(final PbMessageValueReasonString value) throws ValidationException{
        if(value.getReasonString().contains("\0")){
            throw new ValidationException("Message contains Null termination sequences.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),value.getReasonString());
        }
    } 
    
    private void checkLanguageCodeForNullTermination(final PbMessageValueReasonString value) throws ValidationException{
        if(value.getLangCode().contains("\0")){
            throw new ValidationException("Message contains Null termination sequences.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),value.getLangCode());
        }
    }
    
}
