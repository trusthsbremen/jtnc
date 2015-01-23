package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.HSBConstants;
import de.hsbremen.tc.tnc.attribute.TncCommonAttributeTypeEnum;
import de.hsbremen.tc.tnc.attribute.TncHsbAttributeTypeEnum;
import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.ClientWorking;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

class DefaultServerServerWorkingState extends AbstractState implements ClientWorking {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServerServerWorkingState.class);
	private StateHelper<TncsContentHandler> factory;
	
	DefaultServerServerWorkingState(StateHelper<TncsContentHandler> factory){
		super(factory.getHandler());
		this.factory = factory;
	}

	@Override
	public TnccsBatch collect() {
		TnccsBatch b = null;
		
		List<TnccsMessage> messages  = super.getHandler().collectMessages();
		try{
				
			b = this.createClientBatch(messages);
			super.setSuccessor(this.factory.createState(TnccsStateEnum.CLIENT_WORKING));
				
		}catch(ValidationException e){
				
			TnccsMessage error = StateUtil.createLocalError();
			b = StateUtil.createCloseBatch(true, error);
			super.setSuccessor(this.factory.createState(TnccsStateEnum.END));
	
		}
		
		return b;
	}
	
	@Override
	public TnccsBatch handle(TnccsBatchContainer batchContainer) {
		
		TnccsBatch b = null;
		
		if(batchContainer.getResult() == null){
			throw new NullPointerException("Batch cannot be null. The state transitions seem corrupted."); 
		
		}else{
		
			if(batchContainer.getResult() instanceof PbBatch){
				PbBatch current = (PbBatch) batchContainer.getResult();
				
				if(current.getHeader().getType().equals(PbBatchTypeEnum.CDATA) || current.getHeader().getType().equals(PbBatchTypeEnum.CRETRY)){	
					
					boolean roundTripOverrunImminent = this.checkRoundTrips();
			
					try {
						b = this.handleClientData(batchContainer);
						if(!b.getMessages().isEmpty() && !roundTripOverrunImminent){
							super.setSuccessor(this.factory.createState(TnccsStateEnum.CLIENT_WORKING));
						}else{
							// if empty, than there is no more to say, so a decision can be made
							b = this.createResult();
							super.setSuccessor(this.factory.createState(TnccsStateEnum.DECIDED));
						}
						
					} catch (ValidationException e) {
						TnccsMessage error = StateUtil.createLocalError();
						b = StateUtil.createCloseBatch(true, error);
						super.setSuccessor(this.factory.createState(TnccsStateEnum.END));
					}
				}else{
					
					throw new IllegalArgumentException("Batch cannot be of type " + current.getHeader().getType().toString() + ". The state transitions seem corrupted.");
				
				}
			}else{
				
				throw new IllegalArgumentException("Batch must be an instance of " + PbBatch.class.getCanonicalName() + ". The state transitions seem corrupted.");

			}
		}
		
		return b;

	}

	private boolean checkRoundTrips() {
		try{
			long maxRoundTrips = 0;
			long currentRoundTrips = 0;
			
			Object o1 = this.factory.getAttributes().getAttribute(TncCommonAttributeTypeEnum.TNC_ATTRIBUTEID_MAX_ROUND_TRIPS);
			if(o1 instanceof Long){
				
				maxRoundTrips = ((Long)o1).longValue();
				
				if(HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNKNOWN < maxRoundTrips && maxRoundTrips < HSBConstants.TCG_IM_MAX_ROUND_TRIPS_UNLIMITED){
					Object o2 = this.factory.getAttributes().getAttribute(TncHsbAttributeTypeEnum.HSB_ATTRIBUTEID_CURRENT_ROUND_TRIPS);
					if(o2 instanceof Long){
						LOGGER.debug("Round trip overrun check ( Max:"+maxRoundTrips+", Current:"+currentRoundTrips+", Overrun imminent:"+(maxRoundTrips - currentRoundTrips <= 1)+").");
						currentRoundTrips = ((Long)o1).longValue();
						return (maxRoundTrips - currentRoundTrips <= 1);
						
					}else{
						LOGGER.debug("Current round trip attribute not accessible. Round trips cannot be evaluated"); 
						return false;
					}
				}else{
					LOGGER.debug("Round trips not restricted. Round trips cannot be evaluated"); 
					return false;
				}
			}else{
				LOGGER.debug("Max round trip attribute not accessible. Round trips cannot be evaluated"); 
				return false;
			}

		}catch(TncException | UnsupportedOperationException e){
			LOGGER.debug("Not all round trip attributes where accessible. Round trips cannot be evaluated", e); 
			return false;
		}
	}

	private TnccsBatch createResult() throws ValidationException {
		
		TnccsBatch b = null;
		
		List<TnccsMessage> messages  = ((TncsContentHandler)super.getHandler()).solicitRecommendation();
		
		b = this.createResultBatch(messages);
		
		TncConnectionState state = super.getHandler().getAccessDecision();
		if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ALLOWED) || 
				state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ISOLATED) ||
				state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_NONE)){
			
			super.getHandler().setConnectionState(state);
		
		}else{
			
			throw new IllegalStateException("State " +state.toString()+ " does not reflect access decision");
		
		}
		
		return b;
	}

	@Override
	public State getConclusiveState() {
		State s = super.getSuccessor();
		super.setSuccessor(null);
		
		return s;
	}

	private TnccsBatch handleClientData(TnccsBatchContainer batchContainer) throws ValidationException{
		
		PbBatch current = (PbBatch) batchContainer.getResult();
		
		List<? extends TnccsMessage> request = current.getMessages();
		List<TnccsMessage> response = new LinkedList<>();
		
		if( request != null ){
			
			List<TnccsMessage> msgs = super.getHandler().handleMessages(request);
			if(msgs != null){
				response.addAll(msgs);
			}
			
		}
		
		if(batchContainer.getExceptions() != null){
			List<TnccsMessage> msgs =super.getHandler().handleExceptions(batchContainer.getExceptions());
			if(msgs != null){
				response.addAll(msgs);
			}
		}
		
		TnccsBatch b = this.createClientBatch(response);		
		
		return b;
	}
	
	private TnccsBatch createResultBatch(List<TnccsMessage> messages) throws ValidationException {
		return PbBatchFactoryIetf.createResult((messages != null) ? messages : new ArrayList<TnccsMessage>(0));
	}
	
	private TnccsBatch createClientBatch(List<TnccsMessage> messages) throws ValidationException {
		return PbBatchFactoryIetf.createServerData((messages != null) ? messages : new ArrayList<TnccsMessage>(0));
	}
}
