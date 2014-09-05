package de.hsbremen.tc.tnc.tncc.client;

import de.hsbremen.tc.tnc.nar.connection.IfTConnection;

public interface ExternalInterface {

	public abstract void requestHandshake(IfTConnection connection);

}
