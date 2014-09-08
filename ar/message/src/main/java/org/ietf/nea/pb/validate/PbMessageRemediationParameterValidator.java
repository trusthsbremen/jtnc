package org.ietf.nea.pb.validate;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.rules.NoNoSkip;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.validate.TnccsValidator;

public class PbMessageRemediationParameterValidator implements
        TnccsValidator<PbMessage> {

	private static final class Singleton{
		private static final PbMessageRemediationParameterValidator INSTANCE = new PbMessageRemediationParameterValidator();
	}
	private final PbMessageRemediationParameterStringValidator stringParamsValidator;
	
	private final PbMessageRemediationParameterUriValidator uriParamsValidator;
    
	public static PbMessageRemediationParameterValidator getInstance(){
	   return Singleton.INSTANCE;
	}
 
    private PbMessageRemediationParameterValidator() {
    	// Singleton
    	this.stringParamsValidator = PbMessageRemediationParameterStringValidator.getInstance();
    	this.uriParamsValidator = PbMessageRemediationParameterUriValidator.getInstance();
    }	
    
    @Override
    public void validate(final PbMessage message) throws ValidationException {
    	if(message.getVendorId() != IETFConstants.IETF_PEN_VENDORID || message.getType() != PbMessageTypeEnum.IETF_PB_REMEDIATION_PARAMETERS.messageType()){
    		throw new IllegalArgumentException("Unsupported message type with vendor ID "+ message.getVendorId() +" and message type "+ message.getType() +".");
    	}
    	
    	NoNoSkip.check(message.getFlags());
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
