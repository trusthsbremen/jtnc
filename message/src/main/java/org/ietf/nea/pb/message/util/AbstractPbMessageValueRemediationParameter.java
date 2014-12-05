package org.ietf.nea.pb.message.util;

import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessageSubValue;


public abstract class AbstractPbMessageValueRemediationParameter implements TnccsMessageSubValue{
	
	private final long length;

	AbstractPbMessageValueRemediationParameter(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
