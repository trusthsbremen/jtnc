package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public abstract class AbstractPbMessageValue implements TnccsMessageValue {

	private final long length;
	private final boolean noSkip;  
	
	
	protected AbstractPbMessageValue(final long length) {
		this(length, false);
	}
	
	protected AbstractPbMessageValue(final long length, final boolean noSkip){
		this.length = length;
		this.noSkip = noSkip;
	}
	
	public long getLength(){
		return this.length;
	}

	public boolean hasNoSkip() {
		return noSkip;
	}
	
}
