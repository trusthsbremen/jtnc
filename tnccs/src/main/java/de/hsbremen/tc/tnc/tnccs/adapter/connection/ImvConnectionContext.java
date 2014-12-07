package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import java.util.Map;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;


public interface ImvConnectionContext extends ImConnectionContext{

	public abstract void addRecommendation(long id, ImvRecommendationPair recommendationPair) throws TncException;

	public abstract Map<Long,ImvRecommendationPair> clearRecommendations();
}
