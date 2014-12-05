package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import java.util.List;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;


public interface ImvConnectionContext extends ImConnectionContext{

	public abstract void addRecommendation(ImvRecommendationPair recommendationPair) throws TncException;

	public abstract List<ImvRecommendationPair> clearRecommendations();
}
