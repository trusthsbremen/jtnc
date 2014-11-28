package de.hsbremen.tc.tnc.adapter.connection;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.session.context.SessionConnectionContext;

public interface ServerSessionConnectionContext extends
		SessionConnectionContext {

	public abstract void addRecommendation(ImvRecommendationPair createRecommendationPair)  throws TncException;

}
