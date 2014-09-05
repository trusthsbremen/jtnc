package org.ietf.nea.pb.validate;

import java.util.Arrays;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.trustedcomputinggroup.tnc.TNCConstants;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsHeaderValidator;

public class PbMessageImValidator2 implements TnccsHeaderValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageImValidator2 INSTANCE = new PbMessageImValidator2();  
	}
	
	public static PbMessageImValidator2 getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageImValidator2() {
    	// Singleton
    }
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_PA.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
    	
        PbCommonMessageValidator2.getInstance().validate(message);
        this.checkNoSkipSet(message);
        this.checkFixedMessageLength(message);
       
        PbMessageValueIm value = (PbMessageValueIm)message.getValue();
        this.checkSubVendorId(value);
        this.checkSubMessageType(value);
        this.checkExclusivDelivery(value);
    }
    
    private void checkNoSkipSet(final PbMessage message) throws ValidationException {
        if(!message.getFlags().contains(PbMessageFlagsEnum.NOSKIP)){
        	
            throw new ValidationException("NOSKIP must be set for this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Arrays.toString(message.getFlags().toArray()));
        }
    }
    
    private void checkFixedMessageLength(final PbMessage message) throws ValidationException {
        if(message.getLength() < PbMessageValueIm.FIXED_LENGTH + PbMessage.FIXED_LENGTH){
            throw new ValidationException("Message has an invalid length for its type.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(message.getLength()));
        }
    }
    
    private void checkSubVendorId(final PbMessageValueIm value) throws ValidationException{
        if(value.getSubVendorId() == TNCConstants.TNC_VENDORID_ANY){
            throw new ValidationException("Sub Vendor ID is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(value.getSubVendorId()));
        }
        
    }
    
    private void checkSubMessageType(PbMessageValueIm value) throws ValidationException {
        if(value.getSubType() == TNCConstants.TNC_SUBTYPE_ANY){
            throw new ValidationException("Sub Message type is set to reserved value.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(value.getSubType()));
        }  
        
    }
 
    private void checkExclusivDelivery(PbMessageValueIm value) throws ValidationException {
        // check if exclusive flag is set, but no receiver address was set.
    	// TODO has to be checked in interoperability test if this does not always evaluate true.
        if(value.getImFlags().contains(PbMessageImFlagsEnum.EXCL) && (value.getCollectorId() == TNCConstants.TNC_IMCID_ANY || value.getCollectorId() == TNCConstants.TNC_IMCID_ANY)){
            throw new ValidationException("Exclusive flag found, but collector id set to ANY.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(value.getCollectorId()), Long.toString(value.getCollectorId())); 
        }
    }

}
