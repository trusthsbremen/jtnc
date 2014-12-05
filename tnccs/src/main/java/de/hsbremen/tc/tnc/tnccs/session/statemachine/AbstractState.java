package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.session.base.state.TnccsContentHandler;

public abstract class AbstractState implements State{

	private final TnccsContentHandler handler;
	private State successor;
	
	public AbstractState(TnccsContentHandler handler){
		this.handler = handler;
		this.successor = null;
	}
	
	@Override
	public State getProcessorState(TnccsBatch result) {
		throw new UnsupportedOperationException("getProccessorState() is not supported by this state.");
	}

	@Override
	public TnccsBatch collect() {
		throw new UnsupportedOperationException("collect() is not supported by this state.");
	}

	@Override
	public TnccsBatch handle(TnccsBatchContainer batchContainer) {
		throw new UnsupportedOperationException("handle() is not supported by this state.");
	}

	@Override
	public State getConclusiveState() {
		throw new UnsupportedOperationException("getConclusiveState() is not supported by this state.");
	}

	protected TnccsContentHandler getHandler() {
		return this.handler;
	}
	
	protected State getSuccessor(){
		return this.successor;
	}
	
	protected void setSuccessor(State successor){
		this.successor = successor;
	}
}
