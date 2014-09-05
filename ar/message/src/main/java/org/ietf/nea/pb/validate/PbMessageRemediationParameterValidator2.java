package org.ietf.nea.pb.validate;

import java.util.Arrays;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsHeaderValidator;

public class PbMessageRemediationParameterValidator2 implements
        TnccsHeaderValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageRemediationParameterValidator2 INSTANCE = new PbMessageRemediationParameterValidator2();
	}
	private final PbMessageRemediationParameterStringValidator2 stringParamsValidator;
	
	private final PbMessageRemediationParameterUriValidator2 uriParamsValidator;
    
	public static PbMessageRemediationParameterValidator2 getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageRemediationParameterValidator2() {
    	// Singleton
    	this.stringParamsValidator = PbMessageRemediationParameterStringValidator2.getInstance();
    	this.uriParamsValidator = PbMessageRemediationParameterUriValidator2.getInstance();
    }	
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
        PbCommonMessageValidator2.getInstance().validate(message);
        this.checkNoSkipNotSet(message);
        // this check is done by subclasses not needed here
        //this.checkMinMessageLength(message); 
        PbMessageValueRemediationParameters value = (PbMessageValueRemediationParameters)message.getValue();
        // TODO how to check if this was received by a server, must never happen.
        
        // TODO make this more flexible in the future, such as using a builder injecting different remediation validators in a Map<long,Map<long, PbMessageRemediationParametersValidatorSpecific>>
        if(value.getRpVendorId() == IETFConstants.IETF_PEN_VENDORID){
        	if(value.getRpType() == PbMessageRemediationParameterTypeEnum.IETF_STRING.type()){
        		this.stringParamsValidator.validate(message);
        		
        	}else if(value.getRpType() == PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
        		this.uriParamsValidator.validate(message);
        	}
        }else{
        	throw new IllegalArgumentException("Unsupported remediation message type with vendor ID "+ value.getRpVendorId() +" and message type "+ value.getRpType() +".");
        }
        
        
    }

    private void checkNoSkipNotSet(final PbMessage message) throws ValidationException {
        if(message.getFlags().contains(PbMessageFlagsEnum.NOSKIP)){
            throw new ValidationException("NOSKIP not allowed in this message.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Arrays.toString(message.getFlags().toArray()));
        }
    }
//    private void checkMinMessageLength(final PbMessage message) throws ValidationException {
//        if(message.getLength() < PbMessage.FIXED_LENGTH + PbMessageValueRemediationParameters.FIXED_LENGTH){
//            throw new ValidationException("Message has an invalid length for its type.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),Long.toString(message.getLength()));
//        }
//    }
}
