package de.hsbremen.tc.tnc.session.base.state;

import de.hsbremen.tc.tnc.session.base.StateContext;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;


public interface SessionState {

	public StateResult handle(StateContext ctx);
	public StateResult handle(StateContext ctx, TnccsBatchContainer batch);
}
