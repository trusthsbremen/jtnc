package de.hsbremen.tc.tnc.session.base.state;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;

public class DefaultStateResult implements StateResult{

	private TnccsBatch batch;
	private SessionState state;
	
	public DefaultStateResult(SessionState state, TnccsBatch batch){
		this.batch = batch;
		this.state = state;
	}
	
	@Override
	public SessionState getNextState() {
		return state;
	}
	@Override
	public TnccsBatch getBatch() {
		return batch;
	}
	
	
	
}
