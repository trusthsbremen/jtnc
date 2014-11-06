package de.hsbremen.tc.tnc.im.evaluate;

import java.util.List;

import de.hsbremen.tc.tnc.im.module.SupportedMessageType;

@Deprecated
public interface ImEvaluatorManager {

	public abstract List<ImEvaluator> getEvaluators();

	public abstract List<SupportedMessageType> getSupportedTypes();

	public abstract void terminate();
}