package org.ietf.nea.pb.message;

import org.ietf.nea.pb.message.AbstractPbMessageValue;


/**
 * ------------------------------------
 * The PB message Unknown contains a byte array of an
 * unknown/unsupported message value, is SHOULD not
 * be handled by the TNC client or server in a meaningful way. 
 * However it can be used for the purpose to recover or log 
 * errors that occur because of a unknown/unsupported message 
 * type.
 * 
 */

public class PbMessageValueUnknown extends AbstractPbMessageValue{

    
    private final byte[] message; //ImMessage as byte[]

    PbMessageValueUnknown(final boolean ommittable,final byte[] message) {
		super(message.length, ommittable);
		this.message = message;
	}

	/**
     * @return the message
     */
    public byte[] getMessage() {
        return message;
    }
    
}
