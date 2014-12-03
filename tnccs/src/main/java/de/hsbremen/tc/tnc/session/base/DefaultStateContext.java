package de.hsbremen.tc.tnc.session.base;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.tc.tnc.adapter.connection.ImConnectionContext;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.newp.handler.ImcHandler;
import de.hsbremen.tc.tnc.newp.handler.TnccHandler;
import de.hsbremen.tc.tnc.newp.handler.TnccsValueExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class DefaultStateContext implements StateContext{

	private final ImcHandler imHandler;
	private final TnccHandler tnccHandler;
	private final TnccsValueExceptionHandler exceptionHandler;
	private TncConnectionState connectionState;
	private final ImConnectionContext connectionContext;
	
	
	public DefaultStateContext(ImcHandler imHandler, TnccHandler tnccHandler,
			TnccsValueExceptionHandler exceptionHandler, ImConnectionContext connectionContext) {
		this.imHandler = imHandler;
		this.tnccHandler = tnccHandler;
		this.exceptionHandler = exceptionHandler;
		this.connectionState = DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN;
		this.connectionContext = connectionContext;
	}
	@Override
	public void setConnectionState(TncConnectionState connectionState) {
		this.connectionState = connectionState;
		if(connectionState.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE)){
			this.connectionContext.invalidate();
		}
		this.imHandler.setConnectionState(this.connectionState);
		this.tnccHandler.setConnectionState(this.connectionState);
		
	}
	
	@Override
	public TncConnectionState getConnectionStateUpdate(){
		return this.connectionContext.getReportedConnectionState();
	}
	
	@Override
	public List<TnccsMessage> collectMessages() {
		this.imHandler.requestMessages();
		this.tnccHandler.requestMessages();
		return this.connectionContext.clearMessage();
	}
	@Override
	public List<TnccsMessage> handleMessages(List<? extends TnccsMessage> list) {
		if(list != null){
			for (TnccsMessage tnccsMessage : list) {
				// TODO make a better filter here, only bring those message to a handler who can handle it.
				this.imHandler.forwardMessage(tnccsMessage.getValue());
				this.tnccHandler.forwardMessage(tnccsMessage.getValue());
			}
		}
		return this.connectionContext.clearMessage();
	}
	
	@Override
	public List<TnccsMessage> handleExceptions(
			List<ValidationException> exceptions) {
		List<TnccsMessage> errorMessages = this.exceptionHandler.handle(exceptions);
		return (errorMessages != null) ? errorMessages : new ArrayList<TnccsMessage>(0);
	}
	
}
