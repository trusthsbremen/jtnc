package org.ietf.nea.pb.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;

@Deprecated
public class PbValidationExceptionHandler {
	
	private List<RuleException> nonFatalExceptions = new ArrayList<>();
	
	public void handleException(RuleException e, long batchLength, long currentMessageStart) throws SerializationException{

		if(e.isFatal()){
			SerializationException s = null;
			if(e.getErrorCause() == PbErrorCauseEnum.NOSKIP_MISSING.number()){
					s = new SerializationException("ValidationException occured: " + e.getMessage(), e, false);
			}
			
			if(e.getErrorCause() == PbErrorCauseEnum.NOSKIP_NOT_ALLOWED.number()){
				s = new SerializationException("ValidationException occured: " + e.getMessage(), e, false);
			}
			
			if(s != null){
				throw s;
			}else{
				e.printStackTrace();
			}
		}else{
			nonFatalExceptions.add(e); 
		}
	}
	
	public List<RuleException> getNonFatalExceptions(){
		return Collections.unmodifiableList(nonFatalExceptions);
	}
}
