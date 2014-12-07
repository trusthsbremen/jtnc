package de.hsbremen.tc.tnc.tnccs.message.handler.simple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.ImvRecommendationPair;
import de.hsbremen.tc.tnc.tnccs.message.handler.ImvHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsValidationExceptionHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsHandler;

public class DefaultTncsContentHandler implements TncsContentHandler{

	private final ImvHandler imHandler;
	private final TncsHandler tncsHandler;
	private final TnccsValidationExceptionHandler exceptionHandler;
	private TncConnectionState connectionState;
	
	public DefaultTncsContentHandler(ImvHandler imHandler, TncsHandler tncsHandler,
			TnccsValidationExceptionHandler exceptionHandler) {
		this.imHandler = imHandler;
		this.tncsHandler = tncsHandler;
		this.exceptionHandler = exceptionHandler;
		this.connectionState = DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN;
	}
	@Override
	public void setConnectionState(TncConnectionState connectionState) {
		this.connectionState = connectionState;
		this.imHandler.setConnectionState(this.connectionState);
		this.tncsHandler.setConnectionState(this.connectionState);
		
	}
	
	@Override
	public TncConnectionState getAccessDecision(){
		return this.tncsHandler.getAccessDecision();
	}
	
	@Override
	public List<TnccsMessage> collectMessages() {
		List<TnccsMessage> messages = new LinkedList<>();
		
		List<TnccsMessage> temp = this.imHandler.requestMessages();
		if(temp != null){
			messages.addAll(temp);
		}
		
		temp = this.tncsHandler.requestMessages();
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
				
				temp = this.tncsHandler.forwardMessage(tnccsMessage);
				if(temp != null){
					messages.addAll(temp);
				}
			}
		}
		
		List<TnccsMessage> temp = this.imHandler.lastCall();
		if(temp != null){
			messages.addAll(temp);
		}
		
		temp = this.tncsHandler.lastCall();
		if(temp != null){
			messages.addAll(temp);
		}
		
		return messages;
	}
	
	@Override 
	public List<TnccsMessage> solicitRecommendation(){
		List<TnccsMessage> messages = new LinkedList<>();
		
		List<ImvRecommendationPair> imvResults = this.imHandler.solicitRecommendation();
		
		List<TnccsMessage> temp = this.tncsHandler.provideRecommendation(imvResults);
		
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
}
