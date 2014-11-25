package de.hsbremen.tc.tnc.session;

import de.hsbremen.tc.tnc.session.context.TncSession;
import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public interface TnccsSessionFactory {

	public abstract TncSession createSession(TransportConnection connection);

}
