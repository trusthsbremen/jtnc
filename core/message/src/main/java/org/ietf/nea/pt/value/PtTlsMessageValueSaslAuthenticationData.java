package org.ietf.nea.pt.value;

import java.util.Arrays;


/**
 * Reference IETF RFC 6876 section 3.8.9:
 * --------------------------------------
 * This TLV carries an opaque (to PT-TLS) blob of octets being exchanged
 * between the NEA Client and the NEA Server.  This TLV facilitates
 * their communications without interpreting any of the bytes.  The SASL
 * Authentication Data TLV MUST NOT be sent until a SASL mechanism has
 * been established for a session.  The SASL Authentication Data TLV
 * associated with the current authentication mechanism MUST NOT be sent
 * after a SASL Result is sent with a Result Code of Success.
 * Additional SASL Authentication Data TLVs would be sent if the PT-TLS
 * Initiator and Responder desire a subsequent SASL authentication to
 * occur but only after another SASL mechanism selection exchange
 * occurs.
 * 
 * These bytes MUST NOT be interpreted by the PT-TLS layer.
 * 
 */

public class PtTlsMessageValueSaslAuthenticationData extends AbstractPtTlsMessageValue{

    private final byte[] authenticationData;

	PtTlsMessageValueSaslAuthenticationData(final long length, byte[] authenticationData) {
		super(length);
		this.authenticationData = authenticationData;
	}

	/**
	 * @return the authentication data
	 */
	public byte[] getAuthenticationData() {
		return Arrays.copyOf(this.authenticationData, this.authenticationData.length);
	}
	
}
