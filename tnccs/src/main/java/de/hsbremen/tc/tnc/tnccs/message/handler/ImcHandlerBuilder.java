package de.hsbremen.tc.tnc.tnccs.message.handler;

import de.hsbremen.tc.tnc.tnccs.adapter.connection.ImcConnectionContext;

public interface ImcHandlerBuilder extends HandlerBuilder<ImcHandler> {

	public abstract HandlerBuilder<ImcHandler> setConnectionContext(
			ImcConnectionContext connectionContext);

}