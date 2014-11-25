package de.hsbremen.tc.tnc.session;

import de.hsbremen.tc.tnc.transport.connection.TransportConnection;

public interface SessionObserver {

	public abstract void notifyClose(TransportConnection connection);

}
