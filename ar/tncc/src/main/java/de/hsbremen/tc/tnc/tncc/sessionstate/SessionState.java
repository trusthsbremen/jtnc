package de.hsbremen.tc.tnc.tncc.sessionstate;

import de.hsbremen.tc.tnc.tncc.session.DefaultTncSession;

public interface SessionState {

	public void handle(DefaultTncSession sessionContext);
}
