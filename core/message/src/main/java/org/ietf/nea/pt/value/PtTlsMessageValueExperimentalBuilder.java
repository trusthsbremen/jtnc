package org.ietf.nea.pt.value;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

public interface PtTlsMessageValueExperimentalBuilder extends TransportMessageValueBuilder{

	public abstract PtTlsMessageValueExperimentalBuilder setMessage(String message)
			throws RuleException;

}