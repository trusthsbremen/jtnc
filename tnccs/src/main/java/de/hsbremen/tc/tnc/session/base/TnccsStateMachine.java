package de.hsbremen.tc.tnc.session.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.session.base.state.ClientInitState;
import de.hsbremen.tc.tnc.session.base.state.ClientRetryState;
import de.hsbremen.tc.tnc.session.base.state.ClientServerWorkingState;
import de.hsbremen.tc.tnc.session.base.state.Decided;
import de.hsbremen.tc.tnc.session.base.state.End;
import de.hsbremen.tc.tnc.session.base.state.EndState;
import de.hsbremen.tc.tnc.session.base.state.SessionState;
import de.hsbremen.tc.tnc.session.base.state.StateResult;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;


public class TnccsStateMachine implements StateMachine {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Session.class);

	private final StateContext context;
	
	private SessionState sessionState;
	
	
	public TnccsStateMachine(StateContext context){
		this.context = context;
		this.sessionState = new ClientServerWorkingState();
	}
	
	@Override
	public TnccsBatch initiateHandshake() {
		
		this.sessionState = new ClientInitState();
		
		StateResult result = this.sessionState.handle(this.context);
		this.sessionState = result.getNextState();
		
		return result.getBatch();
	}
	
	@Override
	public TnccsBatch handle(TnccsBatchContainer batchContainer) {

		StateResult result  = this.sessionState.handle(this.context, batchContainer);
		this.sessionState = result.getNextState();
			
		return result.getBatch();

	}

	@Override
	public TnccsBatch retryHandshake(ImHandshakeRetryReasonEnum reason){
		this.sessionState = new ClientRetryState();
			
		StateResult result  = this.sessionState.handle(this.context);
		this.sessionState = result.getNextState();
				
		return result.getBatch();
	}

	@Override
	public boolean canRetry() {
		return (this.sessionState instanceof Decided);
	}

	@Override
	public boolean isClosed() {
		return (this.sessionState instanceof End);
	}


	@Override
	public void close() {
		this.sessionState = new EndState();
	}

}
