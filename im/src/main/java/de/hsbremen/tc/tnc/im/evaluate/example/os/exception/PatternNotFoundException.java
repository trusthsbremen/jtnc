package de.hsbremen.tc.tnc.im.evaluate.example.os.exception;

import de.hsbremen.tc.tnc.exception.ComprehensibleException;

public class PatternNotFoundException extends ComprehensibleException {

	public PatternNotFoundException(String arg0,
			Object... reasons) {
		super(arg0, reasons);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 822673939934764484L;

}
