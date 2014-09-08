package org.ietf.nea.pb.validate;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueReasonString;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoNoSkip;
import org.ietf.nea.pb.validate.rules.NoNullTerminatedString;
import org.ietf.nea.pb.validate.rules.NoZeroString;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageReasonStringValidator implements TnccsValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageReasonStringValidator INSTANCE = new PbMessageReasonStringValidator();  
	}
	
	public static PbMessageReasonStringValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageReasonStringValidator() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_REASON_STRING.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
    	long fixedMessageLength = PbMessage.FIXED_LENGTH + PbMessageValueReasonString.FIXED_LENGTH;
        
        NoNoSkip.check(message.getFlags());
        PbMessageValueReasonString value = (PbMessageValueReasonString) message.getValue();
        NoZeroString.check(value.getReasonString());
        NoNullTerminatedString.check(value.getReasonString());
        // Zero length string for language code allowed.
        if(value.getLangCodeLength() > 0){
        	NoNullTerminatedString.check(value.getLangCode());
        }
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
