package org.ietf.nea.pt.value;


/**
 * Reference IETF RFC 6876 section 3.7.1:
 * ------------------------------------
 * This message is sent by a PT-TLS Initiator as the first PT-TLS
 * message in a PT-TLS session.  This message discloses the sender's
 * supported versions of the PT-TLS protocol.  To ensure compatibility,
 * this message MUST always be sent using version 1 of the PT-TLS
 * protocol.  Recipients of this message MUST respond with a Version
 * Response or with a PT-TLS Error message (Version Not Supported or
 * Invalid Message).  
 * 
 * The PT Message Length field MUST contain the value 20 since 
 * that is the total of the length of the fixed-length fields at the start 
 * of the PT message (the Vendor ID, PT Message Type, and PT Message Length, 
 * PT Message Identifier) along with the Access Recommendation 
 * field.
 * 
 */

public class PtTlsMessageValueVersionRequest extends AbstractPtTlsMessageValue{


    private final short minVersion; //8 bit(s)
    private final short maxVersion; //8 bit(s)
    private final short preferedVersion; //8 bit(s)

	PtTlsMessageValueVersionRequest(final long length,final short minVersion, final short maxVersion, final short preferedVersion) {
		super(length);
		this.minVersion = minVersion;
		this.maxVersion = maxVersion;
		this.preferedVersion = preferedVersion;
	}

	/**
	 * @return the minVersion
	 */
	public short getMinVersion() {
		return this.minVersion;
	}

	/**
	 * @return the maxVersion
	 */
	public short getMaxVersion() {
		return this.maxVersion;
	}

	/**
	 * @return the preferedVersion
	 */
	public short getPreferedVersion() {
		return this.preferedVersion;
	}
}
