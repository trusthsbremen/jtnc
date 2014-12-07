package de.hsbremen.tc.tnc.tnccs.message.handler;

import java.util.List;

import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;

public interface TncsHandler extends TnccsHandler{

	public abstract List<TnccsMessage> provideRecommendation(List<ImvRecommendationPair> imvResults);
}
