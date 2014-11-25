package de.hsbremen.tc.tnc.im.evaluate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.GlobalHandshakeRetryListener;

public abstract class AbstractImEvaluationUnitIetf implements ImEvaluationUnit{

	protected static final Logger LOGGER = LoggerFactory.getLogger(ImEvaluationUnit.class);
	
	private final GlobalHandshakeRetryListener globalHandshakeRetryListener;
	
	protected AbstractImEvaluationUnitIetf(GlobalHandshakeRetryListener globalHandshakeRetryListener){
		this.globalHandshakeRetryListener = globalHandshakeRetryListener;
	}
	
	protected void requestGlobaleHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException{
		this.globalHandshakeRetryListener.requestGlobalHandshakeRetry(reason);
	}
}
