package de.hsbremen.tc.tnc.im.evaluate.example.simple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.TnccsAdapter;
import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluatorFactoryIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorFactory;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluatorManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

public class DefaultImvEvaluatorFactory extends AbstractImEvaluatorFactoryIetf {

	private static class Singleton {
		private static final ImEvaluatorFactory INSTANCE = new DefaultImvEvaluatorFactory();
	}
	
	public static ImEvaluatorFactory getInstance(){
		return Singleton.INSTANCE;
	}
	
	private static final Set<SupportedMessageType> supportedMessageTypes = new HashSet<>(
			Arrays.asList(
					new SupportedMessageType[]{
							SupportedMessageTypeFactory.createSupportedMessageType(DefaultImvEvaluationUnit.VENDOR_ID,DefaultImvEvaluationUnit.TYPE)
	}));

	
	@Override
	protected ImvEvaluatorManager createEvaluatorManager(final TnccsAdapter tncc, final ImParameter imParams) {
		
		List<ImvEvaluationUnit> units = new ArrayList<>();
		units.add(new DefaultImvEvaluationUnit(tncc.getHandshakeRetryListener()));
		
		ImvEvaluator evaluator = new DefaultImvEvaluator(imParams.getPrimaryId(), units, new DefaultImValueExceptionHandler());
		
		Map<Long,ImvEvaluator> evaluators = new HashMap<>();
		evaluators.put(evaluator.getId(), evaluator);
		
		return new DefaultImvEvaluatorManager(supportedMessageTypes, evaluators);
	}
}
