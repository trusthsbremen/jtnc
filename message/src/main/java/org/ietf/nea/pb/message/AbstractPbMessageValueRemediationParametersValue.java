package org.ietf.nea.pb.message;

public abstract class AbstractPbMessageValueRemediationParametersValue {
	
	private final long length;

	AbstractPbMessageValueRemediationParametersValue(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
