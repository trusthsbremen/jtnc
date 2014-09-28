package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public abstract class AbstractPbMessageValue implements TnccsMessageValue {

	private final long length;
	private final boolean ommittable;  
	
	
	protected AbstractPbMessageValue(final long length) {
		this(length, true);
	}
	
	protected AbstractPbMessageValue(final long length, final boolean ommittable){
		this.length = length;
		this.ommittable = ommittable;
	}
	
	public long getLength(){
		return this.length;
	}

	public boolean isOmmittable() {
		return ommittable;
	}
	
}
