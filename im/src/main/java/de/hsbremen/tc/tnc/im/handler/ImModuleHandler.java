package de.hsbremen.tc.tnc.im.handler;

import java.util.List;

import de.hsbremen.tc.tnc.im.exception.HandshakeAlreadyStartedException;
import de.hsbremen.tc.tnc.im.module.ImModule;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public interface ImModuleHandler<T> {

	public List<TnccsMessageValue> startHandshake();
	
	public List<TnccsMessageValue> forwardMessages(List<TnccsMessageValue> values);
	
	public void endHandshake(long result);

	public void subscribe(ImModule<T> module) throws HandshakeAlreadyStartedException;
	
	public void unSubscribe(ImModule<T> module);
}
