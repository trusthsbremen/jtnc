package de.hsbremen.tc.tnc.im.evaluate.example.os;

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
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImvEvaluatorManager;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

public class OsImvEvaluatorFactory extends AbstractImEvaluatorFactoryIetf {
	
	private static class Singleton {
		private static final ImEvaluatorFactory INSTANCE = new OsImvEvaluatorFactory();
	}
	
	public static ImEvaluatorFactory getInstance(){
		return Singleton.INSTANCE;
	}
	
	private static final Set<SupportedMessageType> supportedMessageTypes = new HashSet<>(
			Arrays.asList(
					new SupportedMessageType[]{
							new SupportedMessageType(OsImcEvaluationUnit.VENDOR_ID,OsImcEvaluationUnit.TYPE)
	}));
	
	@Override
	protected ImvEvaluatorManager createEvaluatorManager(TnccsAdapter tncc, ImParameter imParams) {
		
		List<ImvEvaluationUnit> units = new ArrayList<>();
		units.add(new OsImvEvaluationUnit(tncc.getHandshakeRetryListener()));
		
		ImvEvaluator evaluator = new DefaultImvEvaluator(imParams.getPrimaryId(), units, new DefaultImValueExceptionHandler());
		
		Map<Long,ImvEvaluator> evaluators = new HashMap<>();
		evaluators.put(evaluator.getId(), evaluator);
		
		return new DefaultImvEvaluatorManager(supportedMessageTypes, evaluators);
	}

	

}
