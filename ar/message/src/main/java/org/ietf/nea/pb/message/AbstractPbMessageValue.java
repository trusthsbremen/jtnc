package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public abstract class AbstractPbMessageValue implements TnccsMessageValue {

	private final long length;

	AbstractPbMessageValue(final long length) {
		this.length = length;
	}
	
	public long getLength(){
		return this.length;
	}
	
}
