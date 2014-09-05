package de.hsbremen.tc.tnc.tnccs.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

abstract class ComprehensibleException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6714101079655195252L;
	
	private final String[] reasonArguments;
	
	/**
	 * @param arg0
	 */
	ComprehensibleException(final String arg0, final String...reasons) {
		super(arg0);
		this.reasonArguments = reasons;
	}


	/**
	 * @param arg0
	 * @param arg1
	 */
	ComprehensibleException(final String arg0, final Throwable arg1, final String...reasons) {
		super(arg0, arg1);
		this.reasonArguments = reasons;
	}
	
	public List<String> getReasons(){
		return Collections.unmodifiableList(Arrays.asList((reasonArguments != null)?reasonArguments : new String[0]));
	}
	
}
