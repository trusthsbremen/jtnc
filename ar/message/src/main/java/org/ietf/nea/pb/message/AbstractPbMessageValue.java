package org.ietf.nea.pb.message;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public abstract class AbstractPbMessageValue implements TnccsMessageValue {

	private final long length;

	AbstractPbMessageValue(long length) {
		this.length = length;
	}
	
	public final long getLength(){
		return this.length;
	}
	
}
