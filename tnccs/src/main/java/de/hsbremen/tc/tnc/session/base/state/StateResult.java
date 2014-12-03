package de.hsbremen.tc.tnc.session.base.state;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;

public interface StateResult {

	SessionState getNextState();
	TnccsBatch getBatch();
}
