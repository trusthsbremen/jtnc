package org.ietf.nea.pb.validate.rules;

import java.util.List;

import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageErrorCodeEnum;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;
import org.ietf.nea.pb.validate.enums.PbErrorCauseEnum;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.RuleException;

public class BatchResultWithoutMessageAssessmentResult {

	public static void check(final PbBatchTypeEnum type, List<? super PbMessage> messages) throws RuleException{
		if(type != PbBatchTypeEnum.RESULT){
			return;
		}
		if(messages != null){
			for (Object pbMessage : messages) {
				if(((PbMessage)pbMessage).getHeader().getVendorId() == IETFConstants.IETF_PEN_VENDORID && ((PbMessage) pbMessage).getHeader().getMessageType() ==  PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id()){
					return;
				}
			}
		}
		throw new RuleException("The batch of type " + type.toString() + " must contain a message with vendor id " + IETFConstants.IETF_PEN_VENDORID + " and message type " + PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.id() + ".", true, PbMessageErrorCodeEnum.IETF_INVALID_PARAMETER.code(), PbErrorCauseEnum.BATCH_RESULT_NO_ASSESSMENT_RESULT.number(), 
				type.toString(), IETFConstants.IETF_PEN_VENDORID, PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.toString());
	}
}
