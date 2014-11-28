package de.hsbremen.tc.tnc.im.session;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.connection.ImvConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluatorManager;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.report.ImvRecommendationPairFactory;

public class DefaultImvSession extends AbstractDefaultImSession<ImvConnectionAdapter> implements ImvSession{
	
	DefaultImvSession(ImvConnectionAdapter connection, TncConnectionState connectionState, ImvEvaluatorManager evaluators){
		super(connection, connectionState, evaluators);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.AbstractDefaultImSession#triggerMessage(de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum)
	 */
	@Override
	public void triggerMessage(ImMessageTriggerEnum reason) throws TncException {
		super.triggerMessage(reason);
		this.lookForRecommendation();
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.AbstractDefaultImSession#handleMessage(de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent)
	 */
	@Override
	public void handleMessage(ImObjectComponent component) throws TncException {
		super.handleMessage(component);
		this.lookForRecommendation();
	}

	@Override
	public void solicitRecommendation() throws TncException {
		
		ImvRecommendationPair recommendation = null;
		
		if(super.getEvaluator() instanceof ImvEvaluator){
			recommendation = ((ImvEvaluatorManager)super.getEvaluator()).getRecommendation(this);	
		}
		
		super.getConnection().provideRecommendation((recommendation != null) ? ImvRecommendationPairFactory.getDefaultRecommendationPair() : recommendation);
	}
	
	private final void lookForRecommendation() throws TncException{
		
		if(super.getEvaluator() instanceof ImvEvaluatorManager){
			
			ImvEvaluatorManager manager = (ImvEvaluatorManager)super.getEvaluator();
			
			if(manager.hasRecommendation()){
				super.getConnection().provideRecommendation(manager.getRecommendation(this));
			}
		}
	}
	
	
}
