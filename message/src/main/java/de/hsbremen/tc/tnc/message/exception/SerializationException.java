package de.hsbremen.tc.tnc.message.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class SerializationException extends ComprehensibleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6714101079655195252L;
	private final boolean streamAffected;
	
	
	
	/**
	 * @param arg0
	 */
	public SerializationException(final String message, final boolean streamAffected,final Object...reasons) {
		super(message, reasons);
		this.streamAffected = streamAffected;
	}


	/**
	 * @param message
	 * @param arg1
	 */
	public SerializationException(final String message, final Throwable throwable, final boolean streamAffected, final Object...reasons) {
		super(message, throwable, reasons);
		this.streamAffected = streamAffected;
	}


	/**
	 * @return the streamAffected
	 */
	public boolean isStreamAffected() {
		return this.streamAffected;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + "SerializationException [streamAffected=" + this.streamAffected
				+ "]";
	}
	
	
	
}
