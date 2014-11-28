package de.hsbremen.tc.tnc.im.evaluate;

import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;


public interface ImvEvaluationComponent{
	public abstract ImvRecommendationPair getRecommendation(ImSessionContext context);
	
	public abstract boolean hasRecommendation();
}
