package de.hsbremen.tc.tnc.tnccs.exception;

public class ValidationException extends ComprehensibleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -860068945081676249L;
	
	private final boolean fatal;
	private final int errorCode;
	
	public ValidationException(final String message, final boolean fatal,
			final int errorCode, final String... reasons) {
		super(message, reasons);
		this.fatal = fatal;
		this.errorCode = errorCode;
	}
	
	public ValidationException(final String message, Throwable original, final boolean fatal,
			final int errorCode, final String... reasons) {
		super(message, original, reasons);
		this.fatal = fatal;
		this.errorCode = errorCode;
	}
	
	public boolean isFatal(){
		return this.fatal;
	}
	
	public int getErrorCode(){
		return this.errorCode;
	}

}
