package org.ietf.nea.pb.handler.filter;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.message.PbMessage;

public interface MessageFilter {

	//client must ignore Ass result, Acc Recom, Lan Pref ignro all but the last
	public boolean noFilter(PbBatch batch, PbMessage message);

}