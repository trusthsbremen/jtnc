package de.hsbremen.tc.tnc.tnccs.message.handler;

import de.hsbremen.tc.tnc.attribute.Attributed;

public interface TnccsHandlerBuilder<T> extends HandlerBuilder<T>{

	public abstract TnccsHandlerBuilder<T> setAttributes(
			Attributed attributes);

}