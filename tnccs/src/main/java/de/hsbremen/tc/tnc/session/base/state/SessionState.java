package de.hsbremen.tc.tnc.session.base.state;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;


public interface SessionState {

	public TnccsBatch handle (StateContext context);
	public TnccsBatch handle(StateContext context, TnccsBatchContainer batch);
}
