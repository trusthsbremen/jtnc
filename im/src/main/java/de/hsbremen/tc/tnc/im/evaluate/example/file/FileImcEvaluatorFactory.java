package de.hsbremen.tc.tnc.im.evaluate.example.file;

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
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImcEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImcEvaluatorManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

public class FileImcEvaluatorFactory extends AbstractImEvaluatorFactoryIetf {
	
	private static final Set<SupportedMessageType> supportedMessageTypes = new HashSet<>(
			Arrays.asList(
					new SupportedMessageType[]{
							SupportedMessageTypeFactory.createSupportedMessageType(FileImcEvaluationUnit.VENDOR_ID,FileImcEvaluationUnit.TYPE)
	}));
	
	private final String filePath;
	private final String messageDigestIdentifier;
	
	public FileImcEvaluatorFactory(String messageDigestIdentifier, String filePath){
		this.filePath = filePath;
		this.messageDigestIdentifier = messageDigestIdentifier;
	}
	
	@Override
	protected ImcEvaluatorManager createEvaluatorManager(TnccsAdapter tncc, ImParameter imParams) {
		
		List<ImcEvaluationUnit> units = new ArrayList<>();
		units.add(new FileImcEvaluationUnit(this.messageDigestIdentifier, this.filePath, tncc.getHandshakeRetryListener()));
		
		ImcEvaluator evaluator = new DefaultImcEvaluator(imParams.getPrimaryId(), units, new DefaultImValueExceptionHandler());
		
		Map<Long,ImcEvaluator> evaluators = new HashMap<>();
		evaluators.put(evaluator.getId(), evaluator);
		
		return new DefaultImcEvaluatorManager(supportedMessageTypes, evaluators);
	}

	

}