package de.hsbremen.tc.tnc.session.state;

import de.hsbremen.tc.tnc.session.context.SessionContext;

public interface SessionState {

	public SessionState handle(SessionContext context);
}
