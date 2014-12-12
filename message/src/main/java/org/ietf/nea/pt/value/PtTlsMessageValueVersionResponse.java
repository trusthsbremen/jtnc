package org.ietf.nea.pt.value;


/**
 * Reference IETF RFC 6876 section 3.7.2:
 * ------------------------------------
 * This message is sent in response to receiving a Version Request
 * message at the start of a new assessment session.  If a recipient
 * receives a Version Request after a successful version negotiation has
 * occurred on the session, the recipient MUST send an Invalid Message
 * error code in a PT-TLS Error message and have TLS close the session.
 * This message MUST be sent using the syntax, semantics, and
 * requirements of the protocol version specified in this message. 
 * 
 * The PT Message Length field MUST contain the value 20 since 
 * that is the total of the length of the fixed-length fields at the start 
 * of the PT message (the Vendor ID, PT Message Type, and PT Message Length, 
 * PT Message Identifier) along with the Access Recommendation 
 * field.
 * 
 */

public class PtTlsMessageValueVersionResponse extends AbstractPtTlsMessageValue{


    private final short selectedVersion;

	PtTlsMessageValueVersionResponse(final long length,final short selectedVersion) {
		super(length);
		this.selectedVersion = selectedVersion;
	}

	/**
	 * @return the selectedVersion
	 */
	public short getSelectedVersion() {
		return this.selectedVersion;
	}

	
}
