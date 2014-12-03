package de.hsbremen.tc.tnc.session.base.state;

import de.hsbremen.tc.tnc.session.base.StateContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;

public class AbstractClientState implements SessionState{

	@Override
	public StateResult handle(StateContext ctx) {

		TnccsMessage error = ClientStateHelper.createUnexpectedStateError();
		TnccsBatch b = ClientStateHelper.createCloseBatch(error);
		SessionState successor = new EndState();
		
		return new DefaultStateResult(successor, b);
	}

	@Override
	public StateResult handle(StateContext ctx, TnccsBatchContainer batch) {
		
		TnccsMessage error = ClientStateHelper.createUnexpectedStateError();
		TnccsBatch b = ClientStateHelper.createCloseBatch(error);
		SessionState successor = new EndState();
		
		return new DefaultStateResult(successor, b);
	}

}
