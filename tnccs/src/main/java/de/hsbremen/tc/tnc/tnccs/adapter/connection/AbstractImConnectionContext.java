package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.tnccs.session.base.AttributeCollection;
import de.hsbremen.tc.tnc.tnccs.session.base.HandshakeRetryListener;

//FIXME This is a tradeoff, because I could not figure out a way to fix the 
//circular dependency between session and IMC/VConnection.
public abstract class AbstractImConnectionContext implements ImConnectionContext{

	protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractImConnectionContext.class);
	
	protected final List<TnccsMessage> messageQueue;
	protected final Attributed attributes;
	protected final HandshakeRetryListener listener;
	private boolean valid;
	private long maxRoundTrips;

	public AbstractImConnectionContext(Attributed attributes, HandshakeRetryListener listener) {
		this.messageQueue = new LinkedList<>();
		if(attributes != null){
			this.attributes = attributes;
		}else{
			this.attributes = new AttributeCollection();
		}
		
		try{
			Object o = attributes.getAttribute(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_ROUND_TRIPS);
			if(o instanceof Long){
				this.maxRoundTrips = ((Long)o).longValue();
			}
		}catch(TncException | UnsupportedOperationException e){
			this.maxRoundTrips = HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN;
		}
		
		this.listener = listener;
		this.valid = true;
	}

	@Override
	public void addMessage(TnccsMessage message) throws TncException {
		if(this.isValid()){
			this.checkRoundTrips();
			this.messageQueue.add(message);
		}else{
			throw new TncException("Cannot add a message. Session and connection maybe closed.", TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION);
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
		this.checkRoundTrips();
		if(this.listener != null){
			this.listener.retryHandshake(reason);
		}
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
	public void validate() {
		if(!this.valid){
			this.valid = true;
		}
	}
	
	@Override
	public void invalidate() {
		if(this.valid){
			this.valid = false;
		}
	}

	@Override
	public boolean isValid() {
		return this.valid;
	}

	protected void checkRoundTrips() throws TncException {
		if(HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN < maxRoundTrips && maxRoundTrips < HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNLIMITED){
			
			try{
				Object o = attributes.getAttribute(TncHsbAttributeTypeEnum.HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS);
				if(o instanceof Long){
					long currentRoundTrips = ((Long)o).longValue();
					if(currentRoundTrips >= this.maxRoundTrips){
						throw new TncException("Maximum round trips exceeded.",TncExceptionCodeEnum.TNC_RESULT_EXCEEDED_MAX_ROUND_TRIPS);
					}
				}
			}catch(TncException | UnsupportedOperationException e){
				LOGGER.debug("Custom attribute "+ 
						TncHsbAttributeTypeEnum.HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS.toString() +
						" not accessible. Round trip check cannot evaluate round trip count."
						); // 
			}
			
		}
	}
}