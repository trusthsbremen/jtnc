package de.hsbremen.tc.tnc.im.session;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.attribute.TncAttributeType;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.im.adapter.connection.ImConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluatorManager;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.report.enums.ImHandshakeRetryReasonEnum;

public abstract class AbstractDefaultImSession<T extends ImConnectionAdapter> implements ImSession, ImSessionContext{
	protected static final Logger LOGGER = LoggerFactory.getLogger(ImSession.class);

	private long messageIndex; 
	
	private TncConnectionState connectionState;
	
	private final T connection;
	
	private final ImEvaluatorManager evaluatorManager;
	
	private EnumSet<ImHandshakeRetryReasonEnum> connectionHandshakeRetryRequested;
	
	AbstractDefaultImSession(final T connection, final TncConnectionState connectionState, final ImEvaluatorManager evaluatorManager){
		this.evaluatorManager = evaluatorManager;
		this.connectionState = connectionState;
		this.connection = connection;
		this.connectionHandshakeRetryRequested = EnumSet.noneOf(ImHandshakeRetryReasonEnum.class);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSessionContext#requestHandshakeRetry(ImHandshakeRetryReasonEnum)
	 */
	@Override
	public final void requestConnectionHandshakeRetry(final ImHandshakeRetryReasonEnum reason){
		this.connectionHandshakeRetryRequested.add(reason);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSessionContext#getAttribute(de.hsbremen.tc.tnc.attribute.TncAttributeType)
	 */
	@Override
	public final Object getAttribute(final TncAttributeType type) throws TncException {
		return this.connection.getAttribute(type);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#getConnectionState()
	 */
	@Override
	public final TncConnectionState getConnectionState() {
		return connectionState;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#setConnectionState(long)
	 */
	@Override
	public final void setConnectionState(final TncConnectionState connectionState) {
		LOGGER.debug("Connection state has changed to: " + connectionState.toString());
		this.connectionState = connectionState;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#triggerMessage(de.hsbremen.tc.tnc.im.session.enums.ImTriggerReasonEnum)
	 */
	@Override
	public void triggerMessage(final ImMessageTriggerEnum reason) throws TncException{
		LOGGER.info("Message trigger activated  for " + reason.toString());
		
		List<ImObjectComponent> cmpList = new LinkedList<>();
			
		List<ImObjectComponent> components = null;
		
		switch (reason) {
			case BEGIN_HANDSHAKE:
				components =  evaluatorManager.evaluate(this);
				break;
			case BATCH_ENDING:
				components = evaluatorManager.lastCall(this);
				break;
		}

		if(components != null && !components.isEmpty()){
				cmpList.addAll(components);
		}
		
		this.dispatchMessagesToConnection(cmpList);
		
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#handleMessage(de.hsbremen.tc.tnc.im.adapter.ImComponent)
	 */
	@Override
	public void handleMessage(final ImObjectComponent component) throws TncException{
		LOGGER.debug("Handle message with vendor ID " + component.getVendorId() + " and type " + component.getType() + ".");
		List<ImObjectComponent> cmpList = new LinkedList<>();

		List<ImObjectComponent> parameterList  = new ArrayList<ImObjectComponent>();
		parameterList.add(component);
		List<ImObjectComponent> components =  this.evaluatorManager.handle(parameterList, this);
		
		if(components != null && !components.isEmpty()){
			cmpList.addAll(components);
		}

		this.dispatchMessagesToConnection(cmpList);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#terminate()
	 */
	@Override
	public void terminate() {
		LOGGER.debug("Terminate called.");
		this.connectionState = DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE;
		// TODO Auto-generated method stub
	}

	private final long getNextMessageIdentifier() {
		return ++messageIndex;
	}
	
	private final void dispatchMessagesToConnection(final List<ImObjectComponent> componentList) throws TncException{
		LOGGER.debug("Dispatch messages to connection " + this.connection.toString() + ". Message count: " + ((componentList != null)? componentList.size() : 0));
		if(this.connectionHandshakeRetryRequested.size() > 0){
			// use the first reason, because only one can be used. It is anyway usually only after remediation.
			try{
				this.connection.requestHandshakeRetry(this.connectionHandshakeRetryRequested.iterator().next());
			}catch(TncException e){
				LOGGER.warn("Handshake request for connection " +  this.connection.toString() +" was not executed.", e);
				for (ImObjectComponent imComponent : componentList) {
					try{
						this.connection.sendMessage(imComponent,this.getNextMessageIdentifier());
					}catch(ValidationException e1){
						LOGGER.error("Message with " + imComponent.getVendorId() + " and type " + imComponent.getType() + " could not be send, because it contains faulty values. \n ", e1);
					}
				}
				
			}finally{
				this.connectionHandshakeRetryRequested.clear();
			}
		}else{
			for (ImObjectComponent imComponent : componentList) {
				try{
					this.connection.sendMessage(imComponent,this.getNextMessageIdentifier());
				}catch(ValidationException e1){
					LOGGER.error("Message with " + imComponent.getVendorId() + " and type " + imComponent.getType() + " could not be send, because it contains faulty values. \n ", e1);
				}
			}
		}
	}
	
	protected final ImEvaluatorManager getEvaluator(){
		return this.evaluatorManager;
	}
	
	protected final T getConnection(){
		return this.connection;
	}
	
}
