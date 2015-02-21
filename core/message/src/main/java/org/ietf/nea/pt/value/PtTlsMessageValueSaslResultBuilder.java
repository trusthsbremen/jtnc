package org.ietf.nea.pt.value;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

public interface PtTlsMessageValueSaslResultBuilder extends TransportMessageValueBuilder{

	public abstract PtTlsMessageValueSaslResultBuilder setResult(int saslResult) throws RuleException;

	public abstract PtTlsMessageValueSaslResultBuilder setOptionalResultData(
			byte[] data) throws RuleException;
	
}
