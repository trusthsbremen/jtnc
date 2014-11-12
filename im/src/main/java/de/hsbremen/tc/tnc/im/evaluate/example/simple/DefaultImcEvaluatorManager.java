package de.hsbremen.tc.tnc.im.evaluate.example.simple;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.ImComponentFlagsEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluatorManager;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;
import de.hsbremen.tc.tnc.im.session.ImSessionContext;

public class DefaultImcEvaluatorManager implements ImcEvaluatorManager{

	Map<Long,ImcEvaluator> evaluators;
	Set<SupportedMessageType> supportedMessageTypes;
	
	public DefaultImcEvaluatorManager(Map<Long,ImcEvaluator> evaluators) {
		this.evaluators = evaluators;
		this.supportedMessageTypes = new HashSet<>();
	}
	
	public DefaultImcEvaluatorManager(Set<SupportedMessageType> supportedMessageTypes, Map<Long,ImcEvaluator> evaluators) {
		this.evaluators = evaluators;
		this.supportedMessageTypes = supportedMessageTypes;
	}
	
	@Override
	public List<ImObjectComponent> evaluate(ImSessionContext context) {
		List<ImObjectComponent> cmpList = new LinkedList<>();
		for (ImcEvaluator evaluator : this.evaluators.values()) {
			List<ImObjectComponent> components = null;
			components = evaluator.evaluate(context);
				
			if(components != null && components.size() > 0){
					cmpList.addAll(components);
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
					List<ImObjectComponent> parameterList  = new ArrayList<ImObjectComponent>();
					parameterList.add(component);
					List<ImObjectComponent> tmpComponents = this.evaluators.get(component.getCollectorId()).handle(parameterList, context);
					if(tmpComponents != null && tmpComponents.size() > 0){
						cmpList.addAll(tmpComponents);
					}
				}
			}else{
				for (ImcEvaluator evaluator : this.evaluators.values()) {
					List<ImObjectComponent> parameterList  = new ArrayList<ImObjectComponent>();
					parameterList.add(component);
					List<ImObjectComponent> tmpComponents = evaluator.handle(parameterList, context);
					if(tmpComponents != null && tmpComponents.size() > 0){
						cmpList.addAll(tmpComponents);
					}
				}
			}
		}
		
		return cmpList;
	}

	@Override
	public List<ImObjectComponent> lastCall(ImSessionContext context) {
		List<ImObjectComponent> cmpList = new LinkedList<>();
		for (ImcEvaluator evaluator : this.evaluators.values()) {
			List<ImObjectComponent> components = null;
			components = evaluator.lastCall(context);
			
			if(components != null && components.size() > 0){
					cmpList.addAll(components);
			}
		}
		
		return cmpList;
	}

	@Override
	public void terminate() {
		for (ImcEvaluator evaluator : this.evaluators.values()) {
			evaluator.terminate();
		}
	}

	@Override
	public Set<SupportedMessageType> getSupportedMessageTypes() {
		return (this.supportedMessageTypes != null) ? this.supportedMessageTypes : new HashSet<SupportedMessageType>();
	}

}
