package de.hsbremen.tc.tnc.im.evaluate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.im.adapter.imc.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.DefaultImEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluator;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

public class DefaultImEvaluatorFactory extends AbstractImEvaluatorFactory {

	private Set<SupportedMessageType> supportedMessageTypes;
	
	public DefaultImEvaluatorFactory(){
		this.supportedMessageTypes = new HashSet<>();
		this.supportedMessageTypes.add(new SupportedMessageType(DefaultImEvaluationUnit.VENDOR_ID,DefaultImEvaluationUnit.TYPE));
	}
	
	@Override
	protected Map<Long, ImEvaluator> createUnits(TnccAdapter tncc, ImParameter imParams) {
		
		List<ImEvaluationUnit> units = new ArrayList<>();
		units.add(new DefaultImEvaluationUnit(tncc.getHandshakeRetryListener()));
		
		ImEvaluator evaluator = new DefaultImEvaluator(imParams.getPrimaryId(), imParams.shouldUseExclusive(), units);
		
		Map<Long,ImEvaluator> evaluators = new HashMap<>();
		evaluators.put(evaluator.getId(), evaluator);
		
		return evaluators;
	}

	@Override
	public Set<SupportedMessageType> getSupportedMessageTypes() {
		return Collections.unmodifiableSet(this.supportedMessageTypes);
	}

}
