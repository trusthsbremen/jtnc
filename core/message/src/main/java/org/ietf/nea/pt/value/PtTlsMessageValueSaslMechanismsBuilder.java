package org.ietf.nea.pt.value;

import org.ietf.nea.pt.value.util.SaslMechanism;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.t.value.TransportMessageValueBuilder;

public interface PtTlsMessageValueSaslMechanismsBuilder extends TransportMessageValueBuilder{

	public abstract PtTlsMessageValueSaslMechanismsBuilder addMechanism(SaslMechanism mech1,
			SaslMechanism... mechs) throws RuleException;
	
}
