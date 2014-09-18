package de.hsbremen.tc.tnc.tnccs.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class ValidationException extends ComprehensibleException {

	public static final int NOT_SPECIFIED = 0; 
	/**
	 * 
	 */
	private static final long serialVersionUID = -860068945081676249L;
	
	private final boolean fatal;
	private final int errorCode;
	private final int errorCause;
	
	public ValidationException(final String message, final boolean fatal,
			final int errorCode, int errorCause , final String... reasons) {
		super(message, reasons);
		this.fatal = fatal;
		this.errorCode = errorCode;
		this.errorCause = errorCause;
	}
	
	public ValidationException(final String message, Throwable original, final boolean fatal,
			final int errorCode, int errorCause ,final String... reasons) {
		super(message, original, reasons);
		this.fatal = fatal;
		this.errorCode = errorCode;
		this.errorCause = errorCause;
	}
	
	public boolean isFatal(){
		return this.fatal;
	}
	
	public int getErrorCode(){
		return this.errorCode;
	}

	public int getErrorCause(){
		return this.errorCause;
	}
}
