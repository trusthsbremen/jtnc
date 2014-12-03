package de.hsbremen.tc.tnc.session.base;

import java.util.List;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public interface StateContext {

	public void setConnectionState(TncConnectionState state);
	
	public abstract TncConnectionState getConnectionStateUpdate();
	
	public abstract List<TnccsMessage> collectMessages();

	public abstract List<TnccsMessage> handleMessages(List<? extends TnccsMessage> list);
	
	public abstract List<TnccsMessage> handleExceptions(List<ValidationException> exceptions);

}
