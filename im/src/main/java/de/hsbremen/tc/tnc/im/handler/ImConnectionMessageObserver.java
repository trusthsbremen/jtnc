package de.hsbremen.tc.tnc.im.handler;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public interface ImConnectionMessageObserver {

	public void addReturnValue(TnccsMessageValue value);
	
}
