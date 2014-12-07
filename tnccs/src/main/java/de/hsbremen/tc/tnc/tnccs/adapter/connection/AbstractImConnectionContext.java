package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import java.util.LinkedList;
import java.util.List;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.base.HandshakeRetryListener;

//FIXME This is a tradeoff, because I could not figure out a way to fix the 
//circular dependency between session and IMC/VConnection.
public abstract class AbstractImConnectionContext implements ImConnectionContext{

	protected final List<TnccsMessage> messageQueue;
	protected final Attributed attributes;
	protected final HandshakeRetryListener listener;
	private boolean valid;

	public AbstractImConnectionContext(Attributed attributes, HandshakeRetryListener listener) {
		this.messageQueue = new LinkedList<>();
		this.attributes = attributes;
		this.listener = listener;
		this.valid = true;
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
	public void requestHandshakeRetry(ImHandshakeRetryReasonEnum reason) throws TncException {
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
	public void invalidate() {
		this.valid = false;
	}

	protected boolean isValid() {
		return this.valid;
	}

}