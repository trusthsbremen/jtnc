package de.hsbremen.tc.tnc.imhandler.handler;

import java.util.List;

import de.hsbremen.tc.tnc.imhandler.exception.HandshakeAlreadyStartedException;
import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public interface ImModuleHandler<T> {

	public List<TnccsMessageValue> startHandshake();
	
	public List<TnccsMessageValue> forwardMessages(List<TnccsMessageValue> values);
	
	public void endHandshake(long result);

	public void subscribe(TnccsImModuleHolder<T> module) throws HandshakeAlreadyStartedException;
	
	public void unSubscribe(TnccsImModuleHolder<T> module);
}
