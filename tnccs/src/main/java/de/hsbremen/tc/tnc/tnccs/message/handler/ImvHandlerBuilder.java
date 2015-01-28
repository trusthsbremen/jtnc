package de.hsbremen.tc.tnc.tnccs.message.handler;

import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImvConnectionContext;

public interface ImvHandlerBuilder extends HandlerBuilder<ImvHandler> {

	public abstract HandlerBuilder<ImvHandler> setConnectionContext(
			ImvConnectionContext connectionContext);

}