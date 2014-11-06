package de.hsbremen.tc.tnc.im.session;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.adapter.ImConnectionStateEnum;
import de.hsbremen.tc.tnc.im.adapter.ImHandshakeRetryReasonEnum;
import de.hsbremen.tc.tnc.im.adapter.connection.ImcConnectionAdapter;
import de.hsbremen.tc.tnc.im.adapter.data.ImObjectComponent;
import de.hsbremen.tc.tnc.im.adapter.data.enums.ImComponentFlagsEnum;
import de.hsbremen.tc.tnc.im.evaluate.ImEvaluator;
import de.hsbremen.tc.tnc.im.session.enums.ImMessageTriggerEnum;

public class DefaultImcSession implements ImSession, ImSessionContext{
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultImcSession.class);

	private long messageIndex; 
	
	private ImConnectionStateEnum connectionState;
	
	private ImcConnectionAdapter connection;
	
	private Map<Long,ImEvaluator> evaluators;
	
	private EnumSet<ImHandshakeRetryReasonEnum> connectionHandshakeRetryRequested;
	
	DefaultImcSession(ImcConnectionAdapter connection, ImConnectionStateEnum connectionState, Map<Long,ImEvaluator> evaluators){
		this.evaluators = evaluators;
		this.connectionState = connectionState;
		this.connection = connection;
		this.connectionHandshakeRetryRequested = EnumSet.noneOf(ImHandshakeRetryReasonEnum.class);
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSessionContext#requestHandshakeRetry(ImHandshakeRetryReasonEnum)
	 */
	@Override
	public void requestConnectionHandshakeRetry(ImHandshakeRetryReasonEnum reason){
		this.connectionHandshakeRetryRequested.add(reason);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#getConnectionState()
	 */
	@Override
	public ImConnectionStateEnum getConnectionState() {
		return connectionState;
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#setConnectionState(long)
	 */
	@Override
	public void setConnectionState(final ImConnectionStateEnum connectionState) {
		this.connectionState = connectionState;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#triggerMessage(de.hsbremen.tc.tnc.im.session.enums.ImTriggerReasonEnum)
	 */
	@Override
	public void triggerMessage(ImMessageTriggerEnum reason) throws TNCException{
		LOGGER.info("Message trigger activated  for " + reason.toString());
		
		List<ImObjectComponent> cmpList = new LinkedList<>();
		
		for (ImEvaluator evaluator : this.evaluators.values()) {
			List<ImObjectComponent> components = null;
			
			switch (reason) {
			case BEGIN_HANDSHAKE:
				components = evaluator.evaluate(this);
				break;
			case BATCH_ENDING:
				components = evaluator.lastCall(this);
				break;
			}

			if(components != null && components.size() > 0){
					cmpList.addAll(components);
			}
		}

		this.dispatchMessagesToConnection(cmpList);
		
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#handleMessage(de.hsbremen.tc.tnc.im.adapter.ImComponent)
	 */
	@Override
	public void handleMessage(final ImObjectComponent component) throws TNCException{
		List<ImObjectComponent> cmpList = new LinkedList<>();
		
		if(component.getImFlags().contains(ImComponentFlagsEnum.EXCL)){
			if(this.evaluators.containsKey(component.getCollectorId())){
				List<ImObjectComponent> components = this.evaluators.get(component.getCollectorId()).handle(component, this);
				if(components != null && components.size() > 0){
					cmpList.addAll(components);
				}
			}
		}else{
			for (ImEvaluator evaluator : this.evaluators.values()) {
				List<ImObjectComponent> components = evaluator.handle(component, this);
				if(components != null && components.size() > 0){
					cmpList.addAll(components);
				}
			}
		}

		this.dispatchMessagesToConnection(cmpList);
	}

	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.im.session.ImSession#terminate()
	 */
	@Override
	public void terminate() {
		LOGGER.debug("Terminate called.");
		this.connectionState = ImConnectionStateEnum.TNC_CONNECTION_STATE_DELETE;
		// TODO Auto-generated method stub
	}

	private long getNextMessageIdentifier() {
		return ++messageIndex;
	}
	
	private void dispatchMessagesToConnection(List<ImObjectComponent> componentList) throws TNCException{
		
		if(this.connectionHandshakeRetryRequested.size() > 0){
			// use the first reason, because only one can be used. It is anyway usually only after remediation.
			try{
				this.connection.requestHandshakeRetry(this.connectionHandshakeRetryRequested.iterator().next());
			}catch(TNCException e){
				LOGGER.warn("Handshake request for connection " +  this.connection.toString() +" was not executed.", e);
				for (ImObjectComponent imComponent : componentList) {
					this.connection.sendMessage(imComponent,this.getNextMessageIdentifier());
				}
			}
		}else{
			for (ImObjectComponent imComponent : componentList) {
				this.connection.sendMessage(imComponent,this.getNextMessageIdentifier());
			}
		}
	}
	
}
