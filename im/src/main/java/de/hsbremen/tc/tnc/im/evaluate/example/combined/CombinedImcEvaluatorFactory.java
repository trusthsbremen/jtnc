package de.hsbremen.tc.tnc.im.evaluate.example.combined;

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
import de.hsbremen.tc.tnc.im.evaluate.example.file.FileImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImcEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImcEvaluatorManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

@Deprecated
/**
 * Instead of combined IMC to different IMC should be used.
 * @author Carl-Heinz Genzel
 *
 */
public class CombinedImcEvaluatorFactory extends AbstractImEvaluatorFactoryIetf {
	
	private static final Set<SupportedMessageType> supportedMessageTypes = new HashSet<>(
			Arrays.asList(
					new SupportedMessageType[]{
							//no other types needed because DefaultImcEvaluationUnit has any/any.
							SupportedMessageTypeFactory.createSupportedMessageType(DefaultImcEvaluationUnit.VENDOR_ID, DefaultImcEvaluationUnit.TYPE)
					}));
	
	private final String filePath;
	private final String messageDigestIdentifier;
	
	public CombinedImcEvaluatorFactory(String messageDigestIdentifier, String filePath){
		this.filePath = filePath;
		this.messageDigestIdentifier = messageDigestIdentifier;
	}
	
	@Override
	protected ImcEvaluatorManager createEvaluatorManager(TnccsAdapter tncc, ImParameter imParams) {
		
		List<ImcEvaluationUnit> units = new ArrayList<>();
		units.add(new OsImcEvaluationUnit(tncc.getHandshakeRetryListener()));
		units.add(new FileImcEvaluationUnit(this.messageDigestIdentifier, this.filePath, tncc.getHandshakeRetryListener()));
		units.add(new DefaultImcEvaluationUnit(tncc.getHandshakeRetryListener()));
		
		ImcEvaluator evaluator = new DefaultImcEvaluator(imParams.getPrimaryId(), units, new DefaultImValueExceptionHandler());
		
		Map<Long,ImcEvaluator> evaluators = new HashMap<>();
		evaluators.put(evaluator.getId(), evaluator);
		
		return new DefaultImcEvaluatorManager(supportedMessageTypes, evaluators);
	}

	

}
