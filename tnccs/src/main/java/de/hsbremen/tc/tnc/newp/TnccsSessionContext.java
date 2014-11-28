package de.hsbremen.tc.tnc.newp;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;

public interface TnccsSessionContext{

	public abstract Object getTnccsAttribute(TncAttributeType type);
	public abstract void setTnccsAttribute(TncAttributeType type, Object value);
}
