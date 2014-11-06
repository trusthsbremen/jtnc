package de.hsbremen.tc.tnc.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class HandlingException extends ComprehensibleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5764256412165159887L;

	/**
	 * @param arg0
	 */
	public HandlingException(final String message, final Object...reasons) {
		super(message, reasons);
	}


	/**
	 * @param message
	 * @param arg1
	 */
	public HandlingException(final String message, final Throwable arg1, final Object...reasons) {
		super(message, arg1, reasons);
	}
}
