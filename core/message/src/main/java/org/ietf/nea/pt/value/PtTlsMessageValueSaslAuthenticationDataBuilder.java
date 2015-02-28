package org.ietf.nea.pt.value;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

public interface PtTlsMessageValueSaslAuthenticationDataBuilder extends TransportMessageValueBuilder{

	public abstract PtTlsMessageValueSaslAuthenticationDataBuilder setAuthenticationData(byte[] data)
			throws RuleException;

}