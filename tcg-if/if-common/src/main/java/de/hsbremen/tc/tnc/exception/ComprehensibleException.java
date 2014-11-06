package de.hsbremen.tc.tnc.exception;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ComprehensibleException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6714101079655195252L;
	
	private final Object[] reasonArguments;
	
	/**
	 * @param arg0
	 */
	protected ComprehensibleException(final String arg0, final Object...reasons) {
		super(arg0);
		this.reasonArguments = reasons;
	}


	/**
	 * @param arg0
	 * @param arg1
	 */
	protected ComprehensibleException(final String arg0, final Throwable arg1, final Object...reasons) {
		super(arg0, arg1);
		this.reasonArguments = reasons;
	}
	
	public List<Object> getReasons(){
		return Collections.unmodifiableList(Arrays.asList((reasonArguments != null)?reasonArguments : new Object[0]));
	}
	
}
