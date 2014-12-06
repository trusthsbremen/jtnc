package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;

public interface StateMachine {

	public abstract TnccsBatch start(boolean selfInitiated) throws StateMachineAccessException;

	public abstract TnccsBatch submitBatch(TnccsBatchContainer newBatch) throws StateMachineAccessException;

	public abstract TnccsBatch retryHandshake(ImHandshakeRetryReasonEnum reason) throws TncException;

	public abstract boolean canRetry();
	
	public abstract boolean isClosed();
	
	public abstract void close();

}