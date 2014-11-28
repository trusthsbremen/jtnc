package de.hsbremen.tc.tnc.adapter.im.exception;

public class TerminatedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 724450380083142006L;

	public TerminatedException() {
		super("The IMC/V is terminated and should be removed.");
	}
}
