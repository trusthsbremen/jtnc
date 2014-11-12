package de.hsbremen.tc.tnc.im.exception;

@Deprecated
public class RetryAlreadyRequestedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8728638666376187478L;

	public RetryAlreadyRequestedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RetryAlreadyRequestedException(String message) {
		super(message);
	}

}
