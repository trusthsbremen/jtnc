package de.hsbremen.tc.tnc.message.exception;

import org.ietf.nea.exception.RuleException;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class ValidationException extends ComprehensibleException{
	
	public static final long OFFSET_NOT_SET = Long.MIN_VALUE;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6714101079655195252L;
	private final long exceptionOffset;

	/**
	 * @param message
	 * @param arg1
	 */
	public ValidationException(final String message, final RuleException throwable, final long exceptionOffset, final Object...reasons) {
		super(message, throwable, reasons);
		
		this.exceptionOffset = exceptionOffset;
	}

	/**
	 * @return the exceptionOffset
	 */
	public long getExceptionOffset() {
		return this.exceptionOffset;
	}

	/* (non-Javadoc)
	 * @see java.lang.Throwable#getCause()
	 */
	@Override
	public synchronized RuleException getCause() {
		// must be RuleException because of the constructor
		return (RuleException)super.getCause();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + "SerializationException [exceptionOffset=" + this.exceptionOffset + "]";
	}
	
	
	
}
