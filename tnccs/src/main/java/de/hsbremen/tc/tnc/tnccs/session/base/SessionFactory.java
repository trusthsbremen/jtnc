package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.transport.TransportConnection;

public interface SessionFactory {

	public abstract Session createTnccsSession(TransportConnection connection);

}