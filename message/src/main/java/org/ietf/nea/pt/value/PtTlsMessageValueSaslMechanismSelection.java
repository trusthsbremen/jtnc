package org.ietf.nea.pt.value;

import java.util.Arrays;

import org.ietf.nea.pt.value.util.SaslMechanism;


/**
 * Reference IETF RFC 6876 section 3.8.8:
 * --------------------------------------
 * This TLV is sent by the NEA Client in order to select a SASL
 * mechanism for use on this session.
 * 
 */

public class PtTlsMessageValueSaslMechanismSelection extends AbstractPtTlsMessageValue{

    private final SaslMechanism mechanism;
    private final byte[] initialSaslMsg;

	PtTlsMessageValueSaslMechanismSelection(final long length, final SaslMechanism mechanism) {
		this(length, mechanism, new byte[0]);
	}

	PtTlsMessageValueSaslMechanismSelection(final long length, final SaslMechanism mechanism, byte[] initialSaslMsg) {
		super(length);
		this.mechanism = mechanism;
		this.initialSaslMsg = initialSaslMsg;
	}
	
	/**
	 * @return the mechanism
	 */
	public SaslMechanism getMechanism() {
		return this.mechanism;
	}

	/**
	 * @return the initialSaslMsg
	 */
	public byte[] getInitialSaslMsg() {
		return Arrays.copyOf(this.initialSaslMsg, this.initialSaslMsg.length);
	}

	
	
	
}
