package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsHeaderValidator;

class PbMessageRemediationParameterStringValidator2 implements
        TnccsHeaderValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageRemediationParameterStringValidator2 INSTANCE =  new PbMessageRemediationParameterStringValidator2();  
	}
	static PbMessageRemediationParameterStringValidator2 getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageRemediationParameterStringValidator2() {
    	// Singleton
    }	
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
        this.checkFixedMessageLength(message);
        PbMessageValueRemediationParameterString value = (PbMessageValueRemediationParameterString)((PbMessageValueRemediationParameters)message.getValue()).getParameter();
        // order important - first check zero than null-termination
        this.checkForZeroString(value);
        this.checkRemediationStringForNullTermination(value);
        this.checkLanguageCodeForNullTermination(value);
    }

    private void checkFixedMessageLength(final PbMessage message) throws ValidationException {
        if(message.getLength() < PbMessageValueRemediationParameterString.FIXED_LENGTH + PbMessageValueRemediationParameters.FIXED_LENGTH + PbMessage.FIXED_LENGTH){
            throw new ValidationException("Message has an invalid length for its type.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(message.getLength()));
        }
    }
    
    private void checkForZeroString(final PbMessageValueRemediationParameterString value) throws ValidationException{
    	if(value == null || value.getRemediationString() == ""){
            throw new ValidationException("Message contains zero-string.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),value.getRemediationString());
        }
    }
    
    private void checkRemediationStringForNullTermination(final PbMessageValueRemediationParameterString value) throws ValidationException{
        if(value.getRemediationString().contains("\0")){
            throw new ValidationException("Message contains Null termination sequences.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),value.getRemediationString());
        }
    } 
    
    private void checkLanguageCodeForNullTermination(final PbMessageValueRemediationParameterString value) throws ValidationException{
        if(value.getLangCode().contains("\0")){
            throw new ValidationException("Message contains Null termination sequences.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),value.getLangCode());
        }
    }
}
