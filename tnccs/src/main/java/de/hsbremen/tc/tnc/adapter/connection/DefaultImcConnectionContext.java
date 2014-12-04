package de.hsbremen.tc.tnc.adapter.connection;

import java.util.LinkedList;
import java.util.List;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.session.base.HandshakeRetryListener;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

//FIXME This is a tradeoff, because I could not figure out a way to fix the 
//circular dependency between session and IMC/VConnection.
public class DefaultImcConnectionContext implements ImcConnectionContext{


	private final List<TnccsMessage> messageQueue;
	private TncConnectionState temporaryState;
	private final Attributed attributes;
	private final HandshakeRetryListener listener;
	private boolean valid;
	
	public DefaultImcConnectionContext(Attributed attributes, HandshakeRetryListener listener) {
		super();
		this.messageQueue = new LinkedList<>();
		this.temporaryState = DefaultTncConnectionStateEnum.HSB_CONNECTION_STATE_UNKNOWN;
		this.attributes = attributes;
		this.listener = listener;
	}

	@Override
	public void addMessage(TnccsMessage message) throws TncException {
		if(this.isValid()){
			this.messageQueue.add(message);
		}else{
			throw new TncException("Session and connection closed, cannot add message.", TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION);
		}
		
	}

	@Override
	public List<TnccsMessage> clearMessage() {
		List<TnccsMessage> messages = new LinkedList<>(this.messageQueue);
		this.messageQueue.clear();
		return messages;
	}

	@Override
	public void reportConnectionState(TncConnectionState connectionState) {
		if(this.isValid()){
			if(connectionState.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ALLOWED) || 
					connectionState.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ISOLATED) ||
					connectionState.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_NONE)){
				this.temporaryState = connectionState;
			}else{
				throw new IllegalArgumentException("The method is not allowed to set the state value to " + connectionState.toString());
			}
		}else{
			throw new IllegalArgumentException("The connection context is made invalid.");
		}
		
		
		
	}

	@Override
	public TncConnectionState getReportedConnectionState() {
		return this.temporaryState;
	}

	@Override
	public void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason)
			throws TncException {
		this.listener.retryHandshake(reason);
	}

	@Override
	public Object getAttribute(TncAttributeType type) throws TncException {
		return this.attributes.getAttribute(type);
	}

	@Override
	public void setAttribute(TncAttributeType type, Object value) throws TncException {
		this.attributes.setAttribute(type, value);
	}
	
	@Override
	public void invalidate(){
		this.valid = false;
	}
	
	private boolean isValid(){
		return this.valid;
	}
	
}