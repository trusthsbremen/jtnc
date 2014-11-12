package de.hsbremen.tc.tnc.im.evaluate.example.os;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import de.hsbremen.tc.tnc.im.adapter.imv.enums.ImvActionRecommendationEnum;
import de.hsbremen.tc.tnc.im.adapter.imv.enums.ImvEvaluationResultEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImvRecommendationObject;

public class RecommendationObjectSortTest {

	private List<ImvRecommendationObject> recommendations;
	
	@Before
	public void setUp(){
		
		this.recommendations = new LinkedList<>();
		
		Random r1 = new Random();
		Random r2 = new Random();
		
		for(int i = 0; i < 15; i++){
			this.recommendations.add(new ImvRecommendationObject(ImvActionRecommendationEnum.fromNumber(r1.nextInt(4)), ImvEvaluationResultEnum.fromResult(r2.nextInt(5))));
		}
		this.recommendations.add(new ImvRecommendationObject(ImvActionRecommendationEnum.TNC_IMV_ACTION_RECOMMENDATION_NO_ACCESS, ImvEvaluationResultEnum.TNC_IMV_EVALUATION_RESULT_NONCOMPLIANT_MAJOR));
		
		
	}
	
	@Test
	public void testSort(){
		Collections.sort(this.recommendations, new RecommendationComparator());
		for (ImvRecommendationObject ro : this.recommendations) {
			System.out.println(ro.toString());
		}
	}
	

	private class RecommendationComparator implements Comparator<ImvRecommendationObject>{

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
}