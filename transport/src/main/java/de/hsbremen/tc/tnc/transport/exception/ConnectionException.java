package de.hsbremen.tc.tnc.transport.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class ConnectionException extends ComprehensibleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8801198662406661211L;

	public ConnectionException(String message,Object...reasons){
		super(message,reasons);
	}
	
	public ConnectionException(String message, Throwable exception, Object...reasons) {
		super(message, exception, reasons);
	}

}
