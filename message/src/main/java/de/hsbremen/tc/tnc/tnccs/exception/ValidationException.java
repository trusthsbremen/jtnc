package de.hsbremen.tc.tnc.tnccs.exception;

import org.ietf.nea.pb.exception.RuleException;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class ValidationException extends ComprehensibleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6714101079655195252L;
	private final long exceptionOffset;

	/**
	 * @param message
	 * @param arg1
	 */
	public ValidationException(final String message, final RuleException throwable, final long exceptionOffset, final String...reasons) {
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + "SerializationException [exceptionOffset=" + this.exceptionOffset + "]";
	}
	
	
	
}
