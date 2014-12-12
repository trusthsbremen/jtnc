package org.ietf.nea.pt.value;

import java.util.Collections;
import java.util.List;

import org.ietf.nea.pt.value.util.SaslMechanism;


/**
 * Reference IETF RFC 6876 section 3.8.7:
 * --------------------------------------
 * This TLV is sent by the NEA Server to indicate the list of SASL
 * mechanisms that it is willing and able to use to authenticate the NEA
 * Client.  Each mechanism name consists of a length followed by a name.
 * The total length of the list is determined by the TLV Length field.
 * 
 */

public class PtTlsMessageValueSaslMechanisms extends AbstractPtTlsMessageValue{

    private final List<SaslMechanism> mechanisms;

	PtTlsMessageValueSaslMechanisms(final long length, final List<SaslMechanism> mechanisms) {
		super(length);
		this.mechanisms = mechanisms;
	}

	/**
	 * @return the mechanisms
	 */
	public List<SaslMechanism> getMechanisms() {
		return Collections.unmodifiableList(this.mechanisms);
	}

	
	
}
