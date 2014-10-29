package org.ietf.nea.pb.message.util;


public abstract class AbstractPbMessageValueRemediationParameter {
	
	private final long length;

	AbstractPbMessageValueRemediationParameter(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
