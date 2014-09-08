package org.ietf.nea.pb.validate;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoSkip;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageErrorValidator implements TnccsValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageErrorValidator INSTANCE =  new PbMessageErrorValidator();  
	}
	public static PbMessageErrorValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageErrorValidator() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_ERROR.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
    	long fixedMessageLength = PbMessage.FIXED_LENGTH + PbMessageValueError.FIXED_LENGTH;
		
		NoSkip.check(message.getFlags());
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
