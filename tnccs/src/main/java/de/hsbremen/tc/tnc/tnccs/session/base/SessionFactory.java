package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public interface SessionFactory {

	public abstract SessionBase createTnccsSession(TransportConnection connection);

}