package de.hsbremen.tc.tnc.imhandler.exception;

public class HandshakeAlreadyStartedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1825412079054287979L;

	
	public HandshakeAlreadyStartedException(String message) {
		super(message);
	}
}
