package de.hsbremen.tc.tnc.tnccs.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class SerializationException extends ComprehensibleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6714101079655195252L;
	private final boolean streamAffected;
	private final long exceptionOffset;
	
	
	
	/**
	 * @param arg0
	 */
	public SerializationException(final String message, final boolean streamAffected, final long exceptionOffset ,final String...reasons) {
		super(message, reasons);
		this.streamAffected = streamAffected;
		this.exceptionOffset = exceptionOffset;
	}


	/**
	 * @param message
	 * @param arg1
	 */
	public SerializationException(final String message, final Throwable throwable, final boolean streamAffected, final long exceptionOffset, final String...reasons) {
		super(message, throwable, reasons);
		this.streamAffected = streamAffected;
		this.exceptionOffset = exceptionOffset;
	}


	/**
	 * @return the streamAffected
	 */
	public boolean isStreamAffected() {
		return this.streamAffected;
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
		return super.toString() + "SerializationException [streamAffected=" + this.streamAffected
				+ ", exceptionOffset=" + this.exceptionOffset + "]";
	}
	
	
	
}
