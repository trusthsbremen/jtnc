package de.hsbremen.tc.tnc.transport.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class ConnectionAttributeNotFoundException extends ComprehensibleException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8866360358864824828L;

	public ConnectionAttributeNotFoundException(String message,Object...reasons){
		super(message,reasons);
	}
	
	public ConnectionAttributeNotFoundException(String message, Throwable exception, Object...reasons) {
		super(message, exception, reasons);
	}
	
}
