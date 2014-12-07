package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import java.util.List;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.exception.StateMachineAccessException;

public interface StateMachine {

	public abstract TnccsBatch start(boolean selfInitiated) throws StateMachineAccessException;

	public abstract TnccsBatch receiveBatch(TnccsBatchContainer newBatch) throws StateMachineAccessException;

	public abstract List<TnccsBatch> retryHandshake(ImHandshakeRetryReasonEnum reason) throws TncException;

	public abstract TnccsBatch close() throws StateMachineAccessException;
	
	public abstract boolean canRetry();
	
	public abstract boolean isClosed();
	
	public abstract void stop();

}