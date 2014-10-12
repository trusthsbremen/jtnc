package de.hsbremen.tc.tnc.exception;


public class AttributeException extends ComprehensibleException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5302862146021400504L;

	protected AttributeException(String arg0, String... reasons){ 
		super(arg0, reasons);
	}
	
	protected AttributeException(String arg0, Throwable arg1, String... reasons) {
		super(arg0, arg1, reasons);
	}
	
}
