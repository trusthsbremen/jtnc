package de.hsbremen.tc.tnc.im.evaluate.example.simple;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluatorIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.util.DefaultRecommendationComparator;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;

public class DefaultImvEvaluator extends AbstractImEvaluatorIetf implements ImvEvaluator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImvEvaluator.class);
	
	public DefaultImvEvaluator(long id ,List<ImvEvaluationUnit> evaluationUnits, ImValueExceptionHandler exceptionHandler){
		super( id, evaluationUnits, exceptionHandler);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImvRecommendationPair getRecommendation(ImSessionContext context) {
		LOGGER.debug("getRecommendation() called, with connection state: " + context.getConnectionState().toString());
		List<ImvRecommendationPair> recommendations = new LinkedList<>();
		// cast save here because it must be initialized with ImvEvaluationUnits
		for (ImvEvaluationUnit unit : (List<ImvEvaluationUnit>)super.getEvaluationUnits()) {
			ImvRecommendationPair recO = ((ImvEvaluationUnit)unit).getRecommendation(context);
			if(recO != null){
				recommendations.add(recO);
			}
		}
		
		if(!recommendations.isEmpty()){
			Comparator<ImvRecommendationPair> comparator = new DefaultRecommendationComparator();
			Collections.sort(recommendations,comparator);		
			// because of the sort get last from list which should be the most severe
			return recommendations.get((recommendations.size() -1));
		}else{
			// Defaults to don't know.
			return ImvRecommendationPairFactory.getDefaultRecommendationPair();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasRecommendation() {
	
		// cast save here because it must be initialized with ImvEvaluationUnits
		for (ImvEvaluationUnit unit :  (List<ImvEvaluationUnit>)super.getEvaluationUnits()) {
			if(!((ImvEvaluationUnit)unit).hasRecommendation()){
				return false;
			}
		}
		
		return true;
	}
}

	
