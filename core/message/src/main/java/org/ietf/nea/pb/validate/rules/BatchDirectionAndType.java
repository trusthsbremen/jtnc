package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.util.NotNull;

public class BatchDirectionAndType {

	public static void check(final PbBatchDirectionalityEnum direction, final PbBatchTypeEnum type) throws RuleException{
		NotNull.check("Direction cannot be null.", direction);
		NotNull.check("Type cannot be null.", type);
		
		if(direction == PbBatchDirectionalityEnum.TO_PBS){
			if(type == PbBatchTypeEnum.RESULT || type == PbBatchTypeEnum.SDATA || type == PbBatchTypeEnum.SRETRY){
				throw new RuleException("The batch type "+type.toString()+" is not expected in the direction "+direction.toString()+".", true,PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code(),PbErrorCauseEnum.BATCH_DIRECTION_OR_TYPE_UNEXPECTED.number(),type.toString(), direction.toString());
			}
		}else if(direction == PbBatchDirectionalityEnum.TO_PBC){
			if(type == PbBatchTypeEnum.CDATA || type == PbBatchTypeEnum.CRETRY){
				throw new RuleException("The batch type "+type.toString()+" is not expected in the direction "+direction.toString()+".", true,PbMessageErrorCodeEnum.IETF_UNEXPECTED_BATCH_TYPE.code(),PbErrorCauseEnum.BATCH_DIRECTION_OR_TYPE_UNEXPECTED.number(),type.toString(), direction.toString());
			}
		}	
	}
}
