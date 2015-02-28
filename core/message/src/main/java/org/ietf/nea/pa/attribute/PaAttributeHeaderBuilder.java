package org.ietf.nea.pa.attribute;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.attribute.ImAttributeHeaderBuilder;

public interface PaAttributeHeaderBuilder extends ImAttributeHeaderBuilder{

	public abstract PaAttributeHeaderBuilder setFlags(byte flags);

	public abstract PaAttributeHeaderBuilder setVendorId(long vendorId) throws RuleException;

	public abstract PaAttributeHeaderBuilder setType(long type) throws RuleException;

	public abstract PaAttributeHeaderBuilder setLength(long length) throws RuleException;

}
