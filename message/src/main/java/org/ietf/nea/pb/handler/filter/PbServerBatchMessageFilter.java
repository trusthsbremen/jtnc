package org.ietf.nea.pb.handler.filter;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.message.PbMessage;

public class PbServerBatchMessageFilter implements MessageFilter {
	
	//client must ignore Ass result, Acc Recom, Lan Pref ignro all but the last
	/* (non-Javadoc)
	 * @see org.ietf.nea.pb.handler.filter.MessageFilter#filter(org.ietf.nea.pb.batch.PbBatch, org.ietf.nea.pb.message.PbMessage)
	 */
	@Override
	public boolean noFilter(PbBatch batch, PbMessage message){
		boolean f = true;
		
		f = f && BatchAssessmentResultNotInResult.filter(batch.getType(), message);
		f = f && BatchAccessRecommendationNotInResult.filter(batch.getType(), message);
		
		return f;
		
	}
}
