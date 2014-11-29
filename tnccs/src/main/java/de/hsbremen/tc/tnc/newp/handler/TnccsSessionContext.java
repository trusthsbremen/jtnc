package de.hsbremen.tc.tnc.newp.handler;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.connection.TncConnectionState;

public interface TnccsSessionContext extends Attributed{

	public abstract void setConnectionState(TncConnectionState state, StateChangePermit permit);
}
