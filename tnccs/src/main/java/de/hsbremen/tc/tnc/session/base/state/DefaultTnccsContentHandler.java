package de.hsbremen.tc.tnc.session.base.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
		List<TnccsMessage> messages = new LinkedList<>();
		
		List<TnccsMessage> temp = this.imHandler.requestMessages();
		if(temp != null){
			messages.addAll(temp);
		}
		
		temp = this.tnccHandler.requestMessages();
		if(temp != null){
			messages.addAll(temp);
		}
		
		return messages;
	}
	@Override
	public List<TnccsMessage> handleMessages(List<? extends TnccsMessage> list) {
		List<TnccsMessage> messages = new LinkedList<>();
		if(list != null){
			for (TnccsMessage tnccsMessage : list) {
				// TODO make a better filter here, only bring those message to a handler who can handle it.
				List<TnccsMessage> temp = this.imHandler.forwardMessage(tnccsMessage.getValue());
				if(temp != null){
					messages.addAll(temp);
				}
				
				temp = this.tnccHandler.forwardMessage(tnccsMessage.getValue());
				if(temp != null){
					messages.addAll(temp);
				}
			}
		}
		return messages;
	}
	
	@Override
	public List<TnccsMessage> handleExceptions(
			List<ValidationException> exceptions) {
		List<TnccsMessage> errorMessages = this.exceptionHandler.handle(exceptions);
		return (errorMessages != null) ? errorMessages : new ArrayList<TnccsMessage>(0);
	}
}
