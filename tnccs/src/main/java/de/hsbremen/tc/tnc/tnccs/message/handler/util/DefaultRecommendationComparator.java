package de.hsbremen.tc.tnc.tnccs.message.handler.util;

import java.util.Comparator;

import de.hsbremen.tc.tnc.report.ImvRecommendationPair;

public class DefaultRecommendationComparator implements Comparator<ImvRecommendationPair>{

	private int weightImvAction(long number){
		
		if(number == 0) return 1;
		if(number == 1) return 3;
		if(number == 2) return 2;
		
		return 0;
	}
	
	private int weightImvEvaluation(long result){
		
		if(result == 0) return 2;
		if(result == 1) return 3;
		if(result == 2) return 4;
		if(result == 3) return 1;
		
		return 0;
	}
	
	@Override
	public int compare(ImvRecommendationPair o1,
			ImvRecommendationPair o2) {
		long o1a = o1.getRecommendationId();
		long o2a = o2.getRecommendationId();
		
		int actionWeight = this.weightImvAction(o1a) - this.weightImvAction(o2a); 
		if(actionWeight != 0) return actionWeight;

				
		long o1e = o1.getResultId();
		long o2e = o2.getResultId();
		return this.weightImvEvaluation(o1e) - this.weightImvEvaluation(o2e); 
		
	}
	
}
