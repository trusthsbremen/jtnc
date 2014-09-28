package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageSubValue;

public abstract class AbstractPbMessageValueRemediationParametersValue implements TnccsMessageSubValue {
	
	private final long length;

	AbstractPbMessageValueRemediationParametersValue(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
