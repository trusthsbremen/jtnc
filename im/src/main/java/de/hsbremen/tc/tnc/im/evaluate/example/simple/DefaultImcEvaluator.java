package de.hsbremen.tc.tnc.im.evaluate.example.simple;

import java.util.List;

import de.hsbremen.tc.tnc.im.evaluate.AbstractImEvaluatorIetf;
import de.hsbremen.tc.tnc.im.evaluate.ImValueExceptionHandler;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluationUnit;
import de.hsbremen.tc.tnc.im.evaluate.ImcEvaluator;

public class DefaultImcEvaluator extends AbstractImEvaluatorIetf implements ImcEvaluator{

	public DefaultImcEvaluator(long id, List<ImcEvaluationUnit> evaluationUnits, ImValueExceptionHandler exceptionHandler) {
		super(id, evaluationUnits, exceptionHandler);
	}

}
