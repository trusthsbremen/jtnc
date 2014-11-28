package de.hsbremen.tc.tnc.report;

import de.hsbremen.tc.tnc.report.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.report.enums.ImvEvaluationResultEnum;


public class ImvRecommendationPairFactory {

	private static final ImvRecommendationPair DEFAULT_PAIR = new ImvRecommendationPair();
	
	public static ImvRecommendationPair getDefaultRecommendationPair(){
		return DEFAULT_PAIR;
	}
	
	public static ImvRecommendationPair createRecommendationPair(ImvActionRecommendationEnum recommendation, ImvEvaluationResultEnum result){
		
	    if(recommendation == null){
	    	throw new NullPointerException("Action recommendation cannot be null.");
	    }
	    
		if(result == null){
			throw new NullPointerException("Evaluation result cannot be null.");
		}
		
		return new ImvRecommendationPair(recommendation,result);
	}
	
	public static ImvRecommendationPair createRecommendationPair(long recommendation, long result){
	
	    ImvActionRecommendationEnum rec = ImvActionRecommendationEnum.fromNumber(recommendation);
	    if(rec == null){
	    	throw new IllegalArgumentException("Action recommendation code " + recommendation + " is unknown.");
	    }
	    
	    ImvEvaluationResultEnum res = ImvEvaluationResultEnum.fromResult(result);
		if(res == null){
			throw new IllegalArgumentException("Evaluation result code " + result + " is unknown.");
		}
	    
	    return new ImvRecommendationPair(rec,res);
	}
	
}
