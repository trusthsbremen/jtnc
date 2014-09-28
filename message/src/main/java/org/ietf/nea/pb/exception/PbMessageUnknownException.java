package org.ietf.nea.pb.exception;

import org.ietf.nea.pb.message.PbMessage;

public class PbMessageUnknownException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -860068945081676249L;
	
	private final PbMessage pbMessage;
	
	public PbMessageUnknownException(final String message, final PbMessage pbMessage) {
		super(message);
		
		this.pbMessage = pbMessage;
	}
	
	public PbMessageUnknownException(final String message, Throwable original, final PbMessage pbMessage) {
		super(message, original);

		this.pbMessage = pbMessage;
	}

	public PbMessage getPbMessage(){
		return this.pbMessage;
	}
}
