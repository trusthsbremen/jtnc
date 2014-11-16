package de.hsbremen.tc.tnc.im.evaluate.example.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.ImComponentFlagsEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.ImvRecommendationObject;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.util.DefaultRecommendationComparator;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;

public class DefaultImvEvaluatorManager implements ImvEvaluatorManager{
	
	Map<Long,ImvEvaluator> evaluators;
	Set<SupportedMessageType> supportedMessageTypes;
	Map<Long,ImvRecommendationObject> evaluatorRecommendations;
	
	public DefaultImvEvaluatorManager(Map<Long,ImvEvaluator> evaluators) {
		this(new HashSet<SupportedMessageType>(), evaluators);
	}
	
	public DefaultImvEvaluatorManager(Set<SupportedMessageType> supportedMessageTypes, Map<Long,ImvEvaluator> evaluators) {
		this.evaluators = evaluators;
		this.supportedMessageTypes = supportedMessageTypes;
		this.evaluatorRecommendations = new HashMap<>(this.evaluators.size());
	}
	
	@Override
	public List<ImObjectComponent> evaluate(ImSessionContext context) {
		List<ImObjectComponent> cmpList = new LinkedList<>();
		for (ImvEvaluator evaluator : this.evaluators.values()) {
			
			List<ImObjectComponent> components = null;
			components = evaluator.evaluate(context);	
			if(components != null && components.size() > 0){
					cmpList.addAll(components);
			}
			
			if(evaluator.hasRecommendation()){
				this.evaluatorRecommendations.put(new Long(evaluator.getId()), evaluator.getRecommendation(context));
			}
			
		}

		
		return cmpList;
	}

	@Override
	public List<ImObjectComponent> handle(
			List<? extends ImObjectComponent> components,
			ImSessionContext context) {

		List<ImObjectComponent> cmpList = new LinkedList<>();
		
		for (ImObjectComponent component : components) {
			// only use excl if there is an effective IM(C/V) ID
			if(component.getImFlags().contains(ImComponentFlagsEnum.EXCL) && component.getCollectorId() != HSBConstants.HSB_IM_ID_UNKNOWN){
				if(this.evaluators.containsKey(component.getCollectorId())){
					
					ImvEvaluator evaluator = this.evaluators.get(component.getCollectorId());
					
					List<ImObjectComponent> parameterList  = new ArrayList<ImObjectComponent>();
					parameterList.add(component);
					List<ImObjectComponent> tmpComponents = evaluator.handle(parameterList, context);
					
					if(tmpComponents != null && tmpComponents.size() > 0){
						cmpList.addAll(tmpComponents);
					}
					
					if(evaluator.hasRecommendation()){
						this.evaluatorRecommendations.put(new Long(evaluator.getId()), evaluator.getRecommendation(context));
					}
				}
			}else{
				for (ImvEvaluator evaluator : this.evaluators.values()) {
					
					List<ImObjectComponent> parameterList  = new ArrayList<ImObjectComponent>();
					parameterList.add(component);
					List<ImObjectComponent> tmpComponents = evaluator.handle(parameterList, context);
					
					if(tmpComponents != null && tmpComponents.size() > 0){
						cmpList.addAll(tmpComponents);
					}
					
					if(evaluator.hasRecommendation()){
						this.evaluatorRecommendations.put(new Long(evaluator.getId()), evaluator.getRecommendation(context));
					}
				}
			}
		}

		return cmpList;
	}

	@Override
	public List<ImObjectComponent> lastCall(ImSessionContext context) {
		List<ImObjectComponent> cmpList = new LinkedList<>();
		
		for (ImvEvaluator evaluator : this.evaluators.values()) {
			List<ImObjectComponent> components = null;
			components = evaluator.lastCall(context);
			
			if(components != null && components.size() > 0){
					cmpList.addAll(components);
			}
			
			if(evaluator.hasRecommendation()){
				this.evaluatorRecommendations.put(new Long(evaluator.getId()), evaluator.getRecommendation(context));
			}
		}
		
		return cmpList;
	}

	@Override
	public void terminate() {
		for (ImvEvaluator evaluator : this.evaluators.values()) {
			evaluator.terminate();
		}
	}

	@Override
	public Set<SupportedMessageType> getSupportedMessageTypes() {
		return (this.supportedMessageTypes != null) ? this.supportedMessageTypes : new HashSet<SupportedMessageType>();
	}
	
	protected ImvRecommendationObject provideRecommendation(ImSessionContext context) {
		if(this.evaluatorRecommendations != null && !this.evaluatorRecommendations.isEmpty()){
			List<ImvRecommendationObject> recommendations = 
					new LinkedList<>(this.evaluatorRecommendations.values());
			
			Comparator<ImvRecommendationObject> comparator = new DefaultRecommendationComparator();
			Collections.sort(recommendations,comparator);		
			// because of the sort get last from list which should be the most severe
			return recommendations.get((recommendations.size() -1));
		}else{
			// Defaults to don't know.
			return new ImvRecommendationObject();
		}
	}
	
	@Override
	public ImvRecommendationObject getRecommendation(ImSessionContext context) {
		for (ImvEvaluator evaluator : this.evaluators.values()) {
			if(evaluator.hasRecommendation()){
				this.evaluatorRecommendations.put(new Long(evaluator.getId()), evaluator.getRecommendation(context));
			}// else ignore the evaluators opinion
		}
		
		return this.provideRecommendation(context);
	}

	@Override
	public boolean hasRecommendation() {
		return(this.evaluators.size() == this.evaluatorRecommendations.size());
	}
}
