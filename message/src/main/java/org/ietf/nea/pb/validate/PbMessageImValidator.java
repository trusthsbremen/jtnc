package org.ietf.nea.pb.validate;

import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.ImExclDelivery;
import org.ietf.nea.pb.validate.rules.ImMessageTypeReservedAndLimits;
import org.ietf.nea.pb.validate.rules.ImVendorIdReservedAndLimits;
import org.ietf.nea.pb.validate.rules.MinMessageLength;
import org.ietf.nea.pb.validate.rules.NoSkip;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageImValidator implements TnccsValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageImValidator INSTANCE = new PbMessageImValidator();  
	}
	
	public static PbMessageImValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageImValidator() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_PA.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
    	long fixedMessageLength = PbMessage.FIXED_LENGTH + PbMessageValueIm.FIXED_LENGTH;
    	
        NoSkip.check(message.getFlags());
		PbMessageValueIm imValue = (PbMessageValueIm) message.getValue();
        ImVendorIdReservedAndLimits.check(imValue.getSubVendorId());
		ImMessageTypeReservedAndLimits.check(imValue.getSubType());
		//TODO test if this will eventually throw always an error.
		ImExclDelivery.check(imValue.getImFlags(), imValue.getCollectorId());
		ImExclDelivery.check(imValue.getImFlags(), imValue.getValidatorId());
		MinMessageLength.check(message.getLength(), fixedMessageLength);
    }
}
