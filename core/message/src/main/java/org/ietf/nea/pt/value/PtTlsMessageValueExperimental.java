package org.ietf.nea.pt.value;


/**
 * Reference IETF RFC 6876 section 3.6:
 * ------------------------------------
 * Reserved for experimental use.'This type will not offer 
 * interoperability but allows for experimentation.  This message
 * type MUST only be sent when the NEA Client and NEA Server 
 * are in the Data Transport phase and only on a restricted, 
 * experimental network. Compliant implementations MUST send an
 * Invalid Message error code in a PT-TLS Error message if an 
 * Experimental message is received.
 * 
 */

public class PtTlsMessageValueExperimental extends AbstractPtTlsMessageValue{

    
    private final String message; //variable string
    
    PtTlsMessageValueExperimental(final long length, final String message) {
		super(length);
		this.message = message;
	}

	/**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
}
