package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class BatchDirectionality {

	public static void check(final byte direction) throws RuleException{
		if(PbBatchDirectionalityEnum.fromDirectionalityBit(direction) == null){
        	throw new RuleException("The direction value " + direction + " is unknown.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.BATCH_DIRECTION_OR_TYPE_UNEXPECTED.number(),direction);
        }
    }
	
}
