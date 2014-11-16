package de.hsbremen.tc.tnc.im.evaluate.example.simple.util;

import java.util.Comparator;

import de.hsbremen.tc.tnc.im.adapter.imv.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.im.adapter.imv.enums.ImvEvaluationResultEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImvRecommendationObject;

public class DefaultRecommendationComparator implements Comparator<ImvRecommendationObject>{

	private int weightImvAction(ImvActionRecommendationEnum action){
		
		if(action.number() == 0) return 1;
		if(action.number() == 1) return 3;
		if(action.number() == 2) return 2;
		
		return 0;
	}
	
	private int weightImvEvaluation(ImvEvaluationResultEnum result){
		
		if(result.code() == 0) return 2;
		if(result.code() == 1) return 3;
		if(result.code() == 2) return 4;
		if(result.code() == 3) return 1;
		
		return 0;
	}
	
	@Override
	public int compare(ImvRecommendationObject o1,
			ImvRecommendationObject o2) {
		ImvActionRecommendationEnum o1a = o1.getRecommendation();
		ImvActionRecommendationEnum o2a = o2.getRecommendation();
		
		int actionWeight = this.weightImvAction(o1a) - this.weightImvAction(o2a); 
		if(actionWeight != 0) return actionWeight;

				
		ImvEvaluationResultEnum o1e = o1.getResult();
		ImvEvaluationResultEnum o2e = o2.getResult();
		return this.weightImvEvaluation(o1e) - this.weightImvEvaluation(o2e); 
		
	}
	
}
