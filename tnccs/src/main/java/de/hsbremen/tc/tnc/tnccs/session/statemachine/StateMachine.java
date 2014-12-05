package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

public interface StateMachine {

	public abstract TnccsBatch start(boolean selfInitiated);

	public abstract TnccsBatch submitBatch(TnccsBatchContainer newBatch);

	public abstract TnccsBatch retryHandshake(ImHandshakeRetryReasonEnum reason) throws TncException;

	public abstract boolean canRetry();
	
	public abstract boolean isClosed();
	
	public abstract void close();

}