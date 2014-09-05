package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.message.PbMessage;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsHeaderValidator;

class PbMessageRemediationParameterUriValidator2 implements
        TnccsHeaderValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageRemediationParameterUriValidator2 INSTANCE = new PbMessageRemediationParameterUriValidator2();  
	}
	static PbMessageRemediationParameterUriValidator2 getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageRemediationParameterUriValidator2() {
    	// Singleton
    }	
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
        // TODO how to check the URI format, because this is already done at serialization.
    	// nothing more to check, but for simplicity reasons added
    }

}
