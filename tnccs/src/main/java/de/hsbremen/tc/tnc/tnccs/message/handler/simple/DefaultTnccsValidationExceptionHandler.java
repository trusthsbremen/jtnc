package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;

public class DefaultTnccsValidationExceptionHandler implements TnccsValidationExceptionHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTnccsValidationExceptionHandler.class);
	
	@Override
	public List<TnccsMessage> handle(List<ValidationException> exceptions) {
		List<TnccsMessage> errorMessages = new LinkedList<>();
		if(exceptions != null){
			
			for (ValidationException validationException : exceptions) {
				LOGGER.warn("An exception was discovered and an error message will be created.", validationException);
				TnccsMessage message = this.createPbError(validationException);
				if(message != null){
					errorMessages.add(message);
				}
			}
		}
		return errorMessages;
	}

	private TnccsMessage createPbError(ValidationException exception) {
		
		long i = exception.getCause().getErrorCode();
		
		try{
		
			if(i == PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code()){
				
				return PbMessageFactoryIetf.createErrorOffset(new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL}, PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER, exception.getExceptionOffset());
				
			}else if(i == PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code()){
				
				return PbMessageFactoryIetf.createErrorSimple(new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL},  PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE);
				
			}else if(i == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE.code()){
				
				return PbMessageFactoryIetf.createErrorOffset(new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL}, PbMessageErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_MESSAGE, exception.getExceptionOffset());

			}else if(i == PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
				RuleException e = exception.getCause();
				// parsing the reasons using the conventions in the batch version check class of messages.
				short actual = 0;
				short maxVersion = 0;
				short minVersion = 0;
				if(e.getReasons() != null){
					int j = 0;
					for (Object o : e.getReasons()) {
						if(o instanceof Short){
							switch(j){
								case 0:
									actual = ((Short)o).shortValue();
									i++;
									break;
								case 1:
									minVersion = maxVersion =  ((Short)o).shortValue();
									i++;
									break;
								case 2: 
									minVersion = ((Short)o).shortValue();
									i++;
									break;
							}
						}
					}
				}
				return PbMessageFactoryIetf.createErrorVersion(new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL}, PbMessageErrorCodeEnum.IETF_UNSUPPORTED_VERSION, actual, maxVersion, minVersion);
				
			}else{
				// defaults to PbMessageErrorCodeEnum.IETF_LOCAL
				return PbMessageFactoryIetf.createErrorSimple(new PbMessageErrorFlagsEnum[]{PbMessageErrorFlagsEnum.FATAL}, PbMessageErrorCodeEnum.IETF_LOCAL);
			}
		}catch(ValidationException e){
			LOGGER.error("Could not create error message for exception \""+e.getMessage()+"\". Because this should never happen, the error message is not send.");
			return null;
		}
	}
	

	
}
