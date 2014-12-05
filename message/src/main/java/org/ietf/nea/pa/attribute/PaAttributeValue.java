package org.ietf.nea.pa.attribute;

import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeValue;

public interface PaAttributeValue extends ImAttributeValue{

	public abstract long getLength();

	public abstract boolean isOmittable();

}
