package org.ietf.nea.pb.validate;

import java.util.Arrays;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsHeaderValidator;

public class PbMessageErrorValidator2 implements TnccsHeaderValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageErrorValidator2 INSTANCE =  new PbMessageErrorValidator2();  
	}
	public static PbMessageErrorValidator2 getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageErrorValidator2() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_ERROR.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
        PbCommonMessageValidator2.getInstance().validate(message);
        this.checkNoSkipSet(message);
        this.checkFixedMessageLength(message);
   
    }
    
    private void checkNoSkipSet(final PbMessage message) throws ValidationException {
        if(!message.getFlags().contains(PbMessageFlagsEnum.NOSKIP)){
            throw new ValidationException("NOSKIP must be set for this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Arrays.toString(message.getFlags().toArray()));
        }
    }
    
    private void checkFixedMessageLength(final PbMessage message) throws ValidationException {
        if(message.getLength() < PbMessageValueError.FIXED_LENGTH + PbMessage.FIXED_LENGTH){
            throw new ValidationException("Message has an invalid length for its type.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(message.getLength()));
        }
    }

}
