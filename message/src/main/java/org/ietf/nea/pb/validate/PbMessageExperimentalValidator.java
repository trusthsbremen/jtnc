package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoNoSkip;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageExperimentalValidator implements TnccsValidator<PbMessage> {
	
	private static final class Singleton{
		private static final PbMessageExperimentalValidator INSTANCE = new PbMessageExperimentalValidator();  
	}  
	
	public static PbMessageExperimentalValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageExperimentalValidator() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_EXPERIMENTAL.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
    	
    	long fixedMessageLength = PbMessage.FIXED_LENGTH;
		
		NoNoSkip.check(message.getFlags());
		MinMessageLength.check(message.getLength(), fixedMessageLength);
    }
}
