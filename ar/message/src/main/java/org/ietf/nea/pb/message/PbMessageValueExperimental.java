package org.ietf.nea.pb.message;


/**
 * Reference IETF RFC 5793 section 4.4:
 * ------------------------------------
 * The PB-Experimental PB-TNC message type is a PB-TNC message type
 * (value 0) that has been set aside for experimental purposes.  It may
 * be used to test code or for other experimental purposes.  It MUST NOT
 * be used in a production environment or in a product. 
 * The contents of the PB-TNC Message Length and PB-TNC Message Value
 * fields for this PB-TNC message type are not specified.  They may have
 * almost any value, depending on what experiments are being conducted.
 * Similarly, the Flags field for this message may have the NOSKIP bit
 * set or cleared, depending on what experiments are being conducted.
 * 
 * A Posture Broker Client or Posture Broker Server implementation
 * intended for production use MUST NOT send a message with this Message
 * Type with the value zero (0) as the vendor ID.  If it receives a
 * message with this message type and with the value zero (0) as the
 * vendor ID, it MUST ignore the message unless the NOSKIP bit is set,
 * in which case it MUST respond with a fatal Unsupported Mandatory
 * Message error code in a CLOSE batch.
 * 
 */

public class PbMessageValueExperimental extends AbstractPbMessageValue{

    
    private final String message; //ImMessage as byte[]

    
    
    PbMessageValueExperimental(final String message) {
		super(message.getBytes().length);
		this.message = message;
	}

	/**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
}
