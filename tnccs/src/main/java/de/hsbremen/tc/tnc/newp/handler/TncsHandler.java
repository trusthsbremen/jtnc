package de.hsbremen.tc.tnc.newp.handler;

import java.util.List;

import de.hsbremen.tc.tnc.report.ImvRecommendationPair;

public interface TncsHandler extends TnccsMessageHandler, StateChangePermit{

	public abstract void provideRecommendation(List<ImvRecommendationPair> imvResults);
}
