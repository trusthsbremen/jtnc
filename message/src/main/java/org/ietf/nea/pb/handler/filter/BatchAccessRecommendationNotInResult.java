package org.ietf.nea.pb.handler.filter;

import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.enums.PbMessageTypeEnum;

import de.hsbremen.tc.tnc.IETFConstants;

public class BatchAccessRecommendationNotInResult {

	public static boolean filter(final PbBatchTypeEnum type, final PbMessage message){
		if(type != PbBatchTypeEnum.RESULT){
			if(message.getVendorId() == IETFConstants.IETF_PEN_VENDORID && message.getType() == PbMessageTypeEnum.IETF_PB_ACCESS_RECOMMENDATION.messageType()){
				return false;
			}
		}
		return true;
	}
}
