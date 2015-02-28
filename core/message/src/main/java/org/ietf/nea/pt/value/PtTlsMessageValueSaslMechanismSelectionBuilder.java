package org.ietf.nea.pt.value;

import org.ietf.nea.pt.value.util.SaslMechanism;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

public interface PtTlsMessageValueSaslMechanismSelectionBuilder extends TransportMessageValueBuilder{

	public abstract PtTlsMessageValueSaslMechanismSelectionBuilder setMechanism(SaslMechanism mech1) throws RuleException;

	public abstract PtTlsMessageValueSaslMechanismSelectionBuilder setInitialSaslMessage(
			byte[] initialSaslMsg) throws RuleException;
	
}
