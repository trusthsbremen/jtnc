package org.ietf.nea.pt.value;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

public interface PtTlsMessageValueVersionResponseBuilder extends TransportMessageValueBuilder {
	
	public abstract PtTlsMessageValueVersionResponseBuilder setVersion(short version)
			throws RuleException;
}
