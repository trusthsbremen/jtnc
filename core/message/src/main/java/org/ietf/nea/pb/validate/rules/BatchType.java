package org.ietf.nea.pb.validate.rules;

import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;

public class BatchType {

	public static void check(final byte type) throws RuleException{
		if(PbBatchTypeEnum.fromType(type) == null){
        	throw new RuleException("The type value " + type + " is unknown.",true,PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(),PbErrorCauseEnum.BATCH_DIRECTION_OR_TYPE_UNEXPECTED.number(), type);
        }
    }
	
}
