package org.ietf.nea.pb.message;

public abstract class AbstractPbMessageValueRemediationParametersValue {
	
	private final long length;

	AbstractPbMessageValueRemediationParametersValue(long length) {
		this.length = length;
	}
	
	public final long getLength(){
		return this.length;
	}
	
}
