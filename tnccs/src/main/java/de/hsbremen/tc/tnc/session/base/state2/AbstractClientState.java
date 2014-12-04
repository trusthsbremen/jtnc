package de.hsbremen.tc.tnc.session.base.state2;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;

public class AbstractClientState implements SessionState{

	private TnccsContentHandler handler;
	
	public AbstractClientState(TnccsContentHandler handler){
		this.handler = handler;
	}
	
	protected TnccsContentHandler getHandler(){
		return this.handler;
	}
	
	@Override
	public TnccsBatch handle(StateContext context) {

		TnccsMessage error = ClientStateHelper.createUnexpectedStateError();
		TnccsBatch b = ClientStateHelper.createCloseBatch(error);
		context.setState(new EndState(this.getHandler()));
		
		return  b;
	}

	@Override
	public TnccsBatch handle(StateContext context, TnccsBatchContainer batch) {
		
		TnccsMessage error = ClientStateHelper.createUnexpectedStateError();
		TnccsBatch b = ClientStateHelper.createCloseBatch(error);
		context.setState(new EndState(this.getHandler()));
		
		return b;
	}
}
