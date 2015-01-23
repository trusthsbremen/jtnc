package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImcHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;

public class DefaultTnccContentHandler implements TnccContentHandler{

	private final ImcHandler imHandler;
	private final TnccHandler tnccHandler;
	private final TnccsValidationExceptionHandler exceptionHandler;
	private TncConnectionState connectionState;
	
	public DefaultTnccContentHandler(ImcHandler imHandler, TnccHandler tnccHandler,
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
				List<TnccsMessage> temp = this.imHandler.forwardMessage(tnccsMessage);
				if(temp != null){
					messages.addAll(temp);
				}
				
				temp = this.tnccHandler.forwardMessage(tnccsMessage);
				if(temp != null){
					messages.addAll(temp);
				}
			}
		}
		
		List<TnccsMessage> temp = this.imHandler.lastCall();
		if(temp != null){
			messages.addAll(temp);
		}
		
		temp = this.tnccHandler.lastCall();
		if(temp != null){
			messages.addAll(temp);
		}
		
		return messages;
	}
	
	@Override
	public List<TnccsMessage> handleExceptions(
			List<ValidationException> exceptions) {
		List<TnccsMessage> errorMessages = this.exceptionHandler.handle(exceptions);
		return (errorMessages != null) ? errorMessages : new ArrayList<TnccsMessage>(0);
	}
	
	@Override
	public void dumpExceptions(
			List<ValidationException> exceptions) {
		this.exceptionHandler.dump(exceptions);
	}
	
	@Override
	public void dumpMessages(List<? extends TnccsMessage> list) {
		if(list != null){
			for (TnccsMessage tnccsMessage : list) {
				// TODO make a better filter here, only bring those message to a handler who can handle it.
				this.imHandler.dumpMessage(tnccsMessage);
				this.tnccHandler.dumpMessage(tnccsMessage);
			}
		}
	}
	
}
