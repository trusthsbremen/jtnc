package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.transport.newp.connection.TransportConnection;

public interface NewSessionFactory {

	public abstract NewSession createTnccsSession(TransportConnection connection);

}