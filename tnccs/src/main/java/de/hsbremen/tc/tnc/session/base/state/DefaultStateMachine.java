package de.hsbremen.tc.tnc.session.base.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.session.base.Session;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;


public class DefaultStateMachine implements StateMachine {

	protected static final Logger LOGGER = LoggerFactory.getLogger(Session.class);

	private final TnccsContentHandler handler;
	
	private SessionState sessionState;

	private StateContext context;
	
	
	public DefaultStateMachine(TnccsContentHandler handler){
		this.handler = handler;
		this.sessionState = new ClientServerWorkingState(this.handler);
	}
	
	@Override
	public TnccsBatch initiateHandshake() {
		
		this.sessionState = new ClientInitState(this.handler);
		
		TnccsBatch result = this.sessionState.handle(this.context);
		
		return result;
	}
	
	@Override
	public TnccsBatch handle(TnccsBatchContainer batchContainer) {

		TnccsBatch result  = this.sessionState.handle(this.context, batchContainer);
			
		return result;

	}

	@Override
	public TnccsBatch retryHandshake(ImHandshakeRetryReasonEnum reason){
		this.sessionState = new ClientRetryState(this.handler);
			
		TnccsBatch result  = this.sessionState.handle(this.context);
				
		return result;
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
		this.sessionState = new EndState(this.handler);
		this.sessionState.handle(this.context);
	}

}
