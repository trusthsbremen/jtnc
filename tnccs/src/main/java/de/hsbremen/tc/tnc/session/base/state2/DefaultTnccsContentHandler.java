package de.hsbremen.tc.tnc.session.base.state2;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.tc.tnc.adapter.connection.ImConnectionContext;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.newp.handler.ImcHandler;
import de.hsbremen.tc.tnc.newp.handler.TnccHandler;
import de.hsbremen.tc.tnc.newp.handler.TnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class DefaultTnccsContentHandler implements TnccsContentHandler{

	private final ImcHandler imHandler;
	private final TnccHandler tnccHandler;
	private final TnccsValidationExceptionHandler exceptionHandler;
	private TncConnectionState connectionState;
	
	public DefaultTnccsContentHandler(ImcHandler imHandler, TnccHandler tnccHandler,
			TnccsValidationExceptionHandler exceptionHandler) {
		this.imHandler = imHandler;
		this.tnccHandler = tnccHandler;
		this.exceptionHandler = exceptionHandler;
		this.connectionState = DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN;
	}
	@Override
	public void setConnectionState(TncConnectionState connectionState) {
		this.connectionState = connectionState;
		this.imHandler.setConnectionState(this.connectionState);
		this.tnccHandler.setConnectionState(this.connectionState);
		
	}
	
	@Override
	public TncConnectionState getAccessDecision(){
		return this.tnccHandler.getAccessDecision();
	}
	
	@Override
	public List<TnccsMessage> collectMessages() {
		this.imHandler.requestMessages();
		this.tnccHandler.requestMessages();
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
	}
	
	@Override
	public List<TnccsMessage> handleExceptions(
			List<ValidationException> exceptions) {
		List<TnccsMessage> errorMessages = this.exceptionHandler.handle(exceptions);
		return (errorMessages != null) ? errorMessages : new ArrayList<TnccsMessage>(0);
	}
	
}
