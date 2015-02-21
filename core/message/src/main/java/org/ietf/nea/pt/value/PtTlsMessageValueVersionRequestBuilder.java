package org.ietf.nea.pt.value;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

public interface PtTlsMessageValueVersionRequestBuilder extends TransportMessageValueBuilder {

	public abstract PtTlsMessageValueVersionRequestBuilder setPreferredVersion(short version)
			throws RuleException;
	
	public abstract PtTlsMessageValueVersionRequestBuilder setMaxVersion(short version)
			throws RuleException;
	
	public abstract PtTlsMessageValueVersionRequestBuilder setMinVersion(short version)
			throws RuleException;
}
