package org.ietf.nea.pb.exception;

import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.message.PbMessageBuilder;
import org.ietf.nea.pb.message.PbMessageValueBuilderHsb;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;

public class PbMessageUnknownException extends ComprehensibleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -860068945081676249L;
	
	private final PbMessage pbMessage;
	
	public PbMessageUnknownException(final String message, final PbMessageBuilder b, final boolean hasNoSkip, final byte[] content, final String... reasons) {
		super(message, reasons);
		
		
		
		PbMessageBuilder builder = b;
		b.setValue(PbMessageValueBuilderHsb.createUnknownValue(hasNoSkip, content));
		PbMessage pb = null;
		try {
			 pb = (PbMessage)builder.toMessage();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.pbMessage = pb;

	}
	
	public PbMessageUnknownException(final String message, Throwable original, final PbMessageBuilder b, final boolean hasNoSkip, final byte[] content ,final String... reasons) {
		super(message, original, reasons);
		
		PbMessageBuilder builder = b;
		b.setValue(PbMessageValueBuilderHsb.createUnknownValue(hasNoSkip, content));
		PbMessage pb = null;
		try {
			 pb = (PbMessage)builder.toMessage();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.pbMessage = pb;
	}

	public PbMessage getPbMessage(){
		return this.pbMessage;
	}
}
