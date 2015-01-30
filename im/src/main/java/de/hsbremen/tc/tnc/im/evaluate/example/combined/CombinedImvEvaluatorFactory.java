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
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.ImvEvaluatorManager;
import de.hsbremen.tc.tnc.im.evaluate.example.file.FileImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.os.OsImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImvEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImvEvaluator;
import de.hsbremen.tc.tnc.im.evaluate.example.simple.DefaultImvEvaluatorManager;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.report.SupportedMessageTypeFactory;

@Deprecated
/**
 * Instead of combined IMC to different IMC should be used.
 * @author Carl-Heinz Genzel
 *
 */
public class CombinedImvEvaluatorFactory extends AbstractImEvaluatorFactoryIetf {
	
	
	private final String osEvaluationValuesPath;
	private final String fileEvaluationValuesPath;
	
	public CombinedImvEvaluatorFactory(String osEvaluationValuesPath, String fileEvaluationValuesPath){
		this.fileEvaluationValuesPath = fileEvaluationValuesPath;
		this.osEvaluationValuesPath = osEvaluationValuesPath;
	}
	
	private static final Set<SupportedMessageType> supportedMessageTypes = new HashSet<>(
			Arrays.asList(
					new SupportedMessageType[]{
							//no other types needed because DefaultImvEvaluationUnit has any/any.
							SupportedMessageTypeFactory.createSupportedMessageType(DefaultImvEvaluationUnit.VENDOR_ID,DefaultImvEvaluationUnit.TYPE),
					}));
	
	@Override
	protected ImvEvaluatorManager createEvaluatorManager(TnccsAdapter tncs, ImParameter imParams) {
		
		List<ImvEvaluationUnit> units = new ArrayList<>();
		units.add(new OsImvEvaluationUnit(this.osEvaluationValuesPath, tncs.getHandshakeRetryListener()));
		units.add(new FileImvEvaluationUnit(this.fileEvaluationValuesPath, tncs.getHandshakeRetryListener()));
		units.add(new DefaultImvEvaluationUnit(tncs.getHandshakeRetryListener()));
		
		ImvEvaluator evaluator = new DefaultImvEvaluator(imParams.getPrimaryId(), units, new DefaultImValueExceptionHandler());
		
		Map<Long,ImvEvaluator> evaluators = new HashMap<>();
		evaluators.put(evaluator.getId(), evaluator);
		
		return new DefaultImvEvaluatorManager(supportedMessageTypes, evaluators);
	}

	

}
