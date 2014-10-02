package org.ietf.nea.pb.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class RuleException extends ComprehensibleException {

	public static final int NOT_SPECIFIED = 0; 
	/**
	 * 
	 */
	private static final long serialVersionUID = -860068945081676249L;
	
	private final boolean fatal;
	private final int errorCode;
	private final int errorCause;
	
	public RuleException(final String message, final boolean fatal,
			final int errorCode, int errorCause , final String... reasons) {
		super(message, reasons);
		this.fatal = fatal;
		this.errorCode = errorCode;
		this.errorCause = errorCause;
	}
	
	public RuleException(final String message, Throwable original, final boolean fatal,
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + "ValidationException [fatal=" + this.fatal + ", errorCode="
				+ this.errorCode + ", errorCause=" + this.errorCause + "]";
	}
	
	
	
	
	
}
