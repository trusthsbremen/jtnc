package de.hsbremen.tc.tnc.im.evaluate.example.simple;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeFactoryIetf;
import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.im.adapter.data.ImFaultyObjectComponent;
import de.hsbremen.tc.tnc.im.evaluate.ImValueExceptionHandler;
import de.hsbremen.tc.tnc.m.attribute.ImAttribute;

public class DefaultImValueExceptionHandler implements ImValueExceptionHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImValueExceptionHandler.class);
	private final static short MAX_VERSION = 1;
	private final static short MIN_VERSION = 1;
	
	@Override
	public List<ImAttribute> handle(ImFaultyObjectComponent component) {
		byte[] messageHeader = (component.getMessageHeader() != null) ? component.getMessageHeader() : new byte[0];
		List<ValidationException> exceptions = (component.getExceptions() != null) ? component.getExceptions() : new ArrayList<ValidationException>(0);
		
		
		List<ImAttribute> errorAttributes = new ArrayList<>();
		for (ValidationException validationException : exceptions) {
			RuleException ruleEx = validationException.getCause();
			ImAttribute error = null;

			try{
				if(ruleEx.getErrorCode() == PaAttributeErrorCodeEnum.IETF_INVALID_PARAMETER.code()){
					error = PaAttributeFactoryIetf.createErrorInformationInvalidParam(messageHeader, validationException.getExceptionOffset());
				}else if(ruleEx.getErrorCode() == PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_MANDATORY_ATTRIBUTE.code()){
					List<Object> reasons = validationException.getReasons();
					PaAttributeHeader attributeHeader = null;
					for (Object object : reasons) {
						if(object instanceof PaAttributeHeader){
							attributeHeader = (PaAttributeHeader) object;
						}
					}
					error = PaAttributeFactoryIetf.createErrorInformationUnsupportedAttribute(messageHeader, attributeHeader);
				}else if(ruleEx.getErrorCode() == PaAttributeErrorCodeEnum.IETF_UNSUPPORTED_VERSION.code()){
					error = PaAttributeFactoryIetf.createErrorInformationUnsupportedVersion(messageHeader, MAX_VERSION, MIN_VERSION);
				}
			}catch(ValidationException e){
				LOGGER.error("Could not create error attribute, because a value error exists.", e);
			}
			
			if(error != null) {
				errorAttributes.add(error);
			}
		}
		
		return errorAttributes;
		
	}

}
