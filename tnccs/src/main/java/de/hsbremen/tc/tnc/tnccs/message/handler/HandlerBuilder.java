package de.hsbremen.tc.tnc.tnccs.message.handler;


public interface HandlerBuilder<T> {

	public abstract T toHandler();

}