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
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluatorManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

public class DefaultImcEvaluatorFactory extends AbstractImEvaluatorFactoryIetf {

	private static class Singleton {
		private static final ImEvaluatorFactory INSTANCE = new DefaultImcEvaluatorFactory();
	}
	
	public static ImEvaluatorFactory getInstance(){
		return Singleton.INSTANCE;
	}
	
	private static final Set<SupportedMessageType> supportedMessageTypes = new HashSet<>(
			Arrays.asList(
					new SupportedMessageType[]{ SupportedMessageTypeFactory.createSupportedMessageType(DefaultImcEvaluationUnit.VENDOR_ID, DefaultImcEvaluationUnit.TYPE)
	}));

	
	@Override
	protected ImcEvaluatorManager createEvaluatorManager(final TnccsAdapter tncc, final ImParameter imParams) {
		
		List<ImcEvaluationUnit> units = new ArrayList<>();
		units.add(new DefaultImcEvaluationUnit(tncc.getHandshakeRetryListener()));
		
		ImcEvaluator evaluator = new DefaultImcEvaluator(imParams.getPrimaryId(), units, new DefaultImValueExceptionHandler());
		
		Map<Long,ImcEvaluator> evaluators = new HashMap<>();
		evaluators.put(evaluator.getId(), evaluator);
		
		return new DefaultImcEvaluatorManager(supportedMessageTypes, evaluators);
	}
}
