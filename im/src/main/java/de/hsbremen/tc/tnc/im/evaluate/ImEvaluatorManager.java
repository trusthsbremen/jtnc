package de.hsbremen.tc.tnc.im.evaluate;

import java.util.Set;

import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

public interface ImEvaluatorManager extends ImEvaluationComponent<ImObjectComponent>{

	public abstract Set<SupportedMessageType> getSupportedMessageTypes();
	
	
	
}