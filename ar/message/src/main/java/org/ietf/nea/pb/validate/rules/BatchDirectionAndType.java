package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;

import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class BatchDirectionAndType {

	public static void check(PbBatchDirectionalityEnum direction, PbBatchTypeEnum type) throws ValidationException{
		if(direction == null){
			throw new NullPointerException("Direction cannot be null.");
		}
		if(type == null){
			throw new NullPointerException("Type cannot be null.");
		}
		
		if(direction == PbBatchDirectionalityEnum.TO_PBS){
			if(type == PbBatchTypeEnum.RESULT || type == PbBatchTypeEnum.SDATA || type == PbBatchTypeEnum.SRETRY){
				throw new ValidationException("The batch type "+type.toString()+" is not expected in the direction "+direction.toString()+".", true,PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code(),type.toString(), direction.toString());
			}
		}else if(direction == PbBatchDirectionalityEnum.TO_PBC){
			if(type == PbBatchTypeEnum.CDATA || type == PbBatchTypeEnum.CRETRY){
				throw new ValidationException("The batch type "+type.toString()+" is not expected in the direction "+direction.toString()+".", true,PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code(),type.toString(), direction.toString());
			}
		}	
	}
}
