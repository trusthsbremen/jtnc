package de.hsbremen.tc.tnc.im.exception;

import de.hsbremen.tc.tnc.exception.AttributeException;


public class AttributeTypeUnexpected extends AttributeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6094542745919080894L;

	public AttributeTypeUnexpected(String arg0, String... reasons) {
		super(arg0, reasons);
	}

}
