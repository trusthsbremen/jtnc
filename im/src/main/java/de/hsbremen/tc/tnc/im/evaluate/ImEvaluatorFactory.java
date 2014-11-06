package de.hsbremen.tc.tnc.im.evaluate;

import java.util.Map;
import java.util.Set;

import de.hsbremen.tc.tnc.im.adapter.imc.ImParameter;
import de.hsbremen.tc.tnc.im.adapter.tncc.TnccAdapter;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

public interface ImEvaluatorFactory {

	public Set<SupportedMessageType> getSupportedMessageTypes();

	public Map<Long, ImEvaluator> getUnits(TnccAdapter tncc, ImParameter imParams);
	
}
