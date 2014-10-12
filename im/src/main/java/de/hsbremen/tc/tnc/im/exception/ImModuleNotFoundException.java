package de.hsbremen.tc.tnc.im.exception;

public class ImModuleNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3092583648942136180L;

	public ImModuleNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImModuleNotFoundException(String message) {
		super(message);
	}

}
