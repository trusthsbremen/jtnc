package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.validate.rules.MinMessageLength;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

class PbMessageRemediationParameterUriValidator implements
        TnccsValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageRemediationParameterUriValidator INSTANCE = new PbMessageRemediationParameterUriValidator();  
	}
	static PbMessageRemediationParameterUriValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageRemediationParameterUriValidator() {
    	// Singleton
    }	
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	
    	long fixedMessageLength = PbMessage.FIXED_LENGTH + PbMessageValueRemediationParameters.FIXED_LENGTH;
    
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
