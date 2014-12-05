package de.hsbremen.tc.tnc.tnccs.session.base.state;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

public interface StateMachine {

	
	public abstract TnccsBatch handle(TnccsBatchContainer batchContainer);
	
	public abstract TnccsBatch initiateHandshake();
	
	public abstract TnccsBatch retryHandshake(ImHandshakeRetryReasonEnum reason) throws TncException;

	public abstract boolean canRetry();

	public abstract boolean isClosed();

	public abstract void close();

	

}
