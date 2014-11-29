package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.TncConnectionState;

public interface TnccsSessionContext{

	public abstract void setConnectionState(TncConnectionState state, StateChangePermit permit);
	public abstract Object getTnccsAttribute(TncAttributeType type);
	public abstract void setTnccsAttribute(TncAttributeType type, Object value);
}
