package de.hsbremen.tc.tnc.im.evaluate;

import de.hsbremen.tc.tnc.im.adapter.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.TnccsAdapter;

public interface ImEvaluatorFactory{

	public ImEvaluatorManager getEvaluators(TnccsAdapter tnccs, ImParameter imParams);
}
