package org.ietf.nea.pa.message;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.m.message.ImMessageHeaderBuilder;

public interface PaMessageHeaderBuilder extends ImMessageHeaderBuilder {

	public abstract PaMessageHeaderBuilder setVersion(short version)
			throws RuleException;

	public abstract PaMessageHeaderBuilder setIdentifier(long identifier)
			throws RuleException;

	public abstract PaMessageHeaderBuilder setLength(long length) throws RuleException;
}