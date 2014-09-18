package de.hsbremen.tc.tnc.client.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class NoImIdsLeftException extends ComprehensibleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2751977144540515135L;

	public NoImIdsLeftException(String arg0, String...reasons) {
		super(arg0, reasons);
	}
	
	public NoImIdsLeftException(String arg0, Throwable t, String...reasons){
		super(arg0,t,reasons);
	}

}
