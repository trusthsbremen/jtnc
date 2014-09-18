package de.hsbremen.tc.tnc.tnccs.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class HandlingException extends ComprehensibleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5764256412165159887L;

	/**
	 * @param arg0
	 */
	public HandlingException(final String message, final String...reasons) {
		super(message, reasons);
	}


	/**
	 * @param message
	 * @param arg1
	 */
	public HandlingException(final String message, final Throwable arg1, final String...reasons) {
		super(message, arg1, reasons);
	}
}
