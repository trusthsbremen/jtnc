package de.hsbremen.tc.tnc.im.exception;

public class SessionNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7496504833621466024L;

	public SessionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionNotFoundException(String message) {
		super(message);
	}

	
	
}
