package de.hsbremen.tc.tnc.imhandler.handler;

import java.util.List;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public interface ImConnectionMessageQueue {

	public void addMessage(TnccsMessageValue value);
	public List<TnccsMessageValue> getMessages();
	public void clear();
}
