package org.ietf.nea.pb.message.util;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValue;


public abstract class AbstractPbMessageValueErrorParameter implements TnccsMessageSubValue{
	
	private final long length;

	AbstractPbMessageValueErrorParameter(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
