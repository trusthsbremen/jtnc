package de.hsbremen.tc.tnc.im.evaluate.example.simple;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluatorIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImvRecommendationObject;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;

public class DefaultImvEvaluator extends AbstractImEvaluatorIetf implements ImvEvaluator {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImvEvaluator.class);
	
	public DefaultImvEvaluator(long id ,List<ImvEvaluationUnit> evaluationUnits){
		super( id, evaluationUnits);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ImvRecommendationObject getRecommendation(ImSessionContext context) {
		LOGGER.debug("getRecommendation() called, with connection state: " + context.getConnectionState().toString());
		
		//TODO make recommendation ranking according to least privilieges;
		ImvRecommendationObject rec = null;
		// cast save here because it must be initialized with ImvEvaluationUnits
		for (ImvEvaluationUnit unit : (List<ImvEvaluationUnit>)super.getEvaluationUnits()) {
			rec = ((ImvEvaluationUnit)unit).getRecommendation(context);
		}
		
		return rec;
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

	
