package de.hsbremen.tc.tnc.im.evaluate;

import de.hsbremen.tc.tnc.im.session.ImSessionContext;

public interface ImvEvaluatorManager extends ImEvaluatorManager{

	public abstract ImvRecommendationObject getRecommendation(ImSessionContext context);
	
	public abstract boolean hasRecommendation();
	
}