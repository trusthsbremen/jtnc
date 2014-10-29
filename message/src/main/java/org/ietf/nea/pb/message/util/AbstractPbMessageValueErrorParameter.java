package org.ietf.nea.pb.message.util;


public abstract class AbstractPbMessageValueErrorParameter {
	
	private final long length;

	AbstractPbMessageValueErrorParameter(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
