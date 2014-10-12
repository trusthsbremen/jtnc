package org.ietf.nea.pb.handler.filter;

import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class BatchAssessmentResultNotInResult {

	public static boolean filter(final PbBatchTypeEnum type, final PbMessage message){
		if(type != PbBatchTypeEnum.RESULT){
			if(message.getHeader().getVendorId() == IETFConstants.IETF_PEN_VENDORID && message.getHeader().getMessageType() == PbMessageTypeEnum.IETF_PB_ASSESSMENT_RESULT.messageType()){
				return false;
			}
		}
		return true;
	}
}
