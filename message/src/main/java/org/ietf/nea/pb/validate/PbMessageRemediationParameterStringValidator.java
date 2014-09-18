package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pb.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

class PbMessageRemediationParameterStringValidator implements
        TnccsValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageRemediationParameterStringValidator INSTANCE =  new PbMessageRemediationParameterStringValidator();  
	}
	static PbMessageRemediationParameterStringValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageRemediationParameterStringValidator() {
    	// Singleton
    }	
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	
    	long fixedMessageLength = PbMessage.FIXED_LENGTH + PbMessageValueRemediationParameters.FIXED_LENGTH + PbMessageValueRemediationParameterString.FIXED_LENGTH;
    	
        PbMessageValueRemediationParameterString value = (PbMessageValueRemediationParameterString)((PbMessageValueRemediationParameters)message.getValue()).getParameter();
        // order important - first check zero than null-termination
        NoZeroString.check(value.getRemediationString());
        NoNullTerminatedString.check(value.getRemediationString());
        // Zero length string for language code allowed.
        if(value.getLangCodeLength() > 0){
        	NoNullTerminatedString.check(value.getLangCode());
        }
        MinMessageLength.check(message.getLength(), fixedMessageLength);
    }
}
