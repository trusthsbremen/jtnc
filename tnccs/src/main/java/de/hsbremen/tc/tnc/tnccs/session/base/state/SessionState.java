package de.hsbremen.tc.tnc.tnccs.session.base.state;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;


public interface SessionState {

	public TnccsBatch handle (StateContext context);
	public TnccsBatch handle(StateContext context, TnccsBatchContainer batch);
}
