package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValue;

public abstract class AbstractPbMessageSubValue implements TnccsMessageSubValue {
	
	private final long length;

	AbstractPbMessageSubValue(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
