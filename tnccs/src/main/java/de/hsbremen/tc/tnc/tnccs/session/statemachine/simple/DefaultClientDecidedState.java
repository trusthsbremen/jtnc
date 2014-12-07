package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Decided;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

class DefaultClientDecidedState extends AbstractState implements Decided {
	
	private StateHelper<TnccContentHandler> factory;
	
	DefaultClientDecidedState(StateHelper<TnccContentHandler> factory) {
		super(factory.getHandler());
		this.factory = factory;
	}

	@Override
	public State getProcessorState(TnccsBatch result) {
		if(result != null && result instanceof PbBatch){
			PbBatch b = (PbBatch) result;
			
			if(b.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)){	
		
				return this.factory.createState(TnccsStateEnum.SERVER_WORKING);
			}

			if(b.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){
			
				return this.factory.createState(TnccsStateEnum.END);
			}
			
		}
			
		return this.factory.createState(TnccsStateEnum.ERROR);
	}

	@Override
	public TnccsBatch handle(TnccsBatchContainer batchContainer) {
		TnccsBatch b = null;
		
		if(batchContainer.getResult() == null){
			throw new NullPointerException("Batch cannot be null. The state transitions seem corrupted."); 
		
		}else{
		
			if(batchContainer.getResult() instanceof PbBatch){
				PbBatch current = (PbBatch) batchContainer.getResult();
				
				if(current.getHeader().getType().equals(PbBatchTypeEnum.RESULT)){	
					this.handleResult(batchContainer);
					super.setSuccessor(this);
				}else{
					
					throw new IllegalArgumentException("Batch cannot be of type " + current.getHeader().getType().toString() + ". The state transitions seem corrupted.");
				
				}
			}else{
				
				throw new IllegalArgumentException("Batch must be an instance of " + PbBatch.class.getCanonicalName() + ". The state transitions seem corrupted.");

			}
		}
		
		return b;
	}

	@Override
	public State getConclusiveState() {
		State s = super.getSuccessor();
		super.setSuccessor(null);
		
		return s;
	}
	
	private void handleResult(TnccsBatchContainer batchContainer) {
		PbBatch b = (PbBatch) batchContainer.getResult();
		if(b.getMessages() != null){
			super.getHandler().handleMessages(b.getMessages());
		}
		
		if(batchContainer.getExceptions() != null){
			super.getHandler().handleExceptions(batchContainer.getExceptions());
		}
		
		TncConnectionState state = super.getHandler().getAccessDecision();
		if(state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ALLOWED) || 
				state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_ISOLATED) ||
				state.equals(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_ACCESS_NONE)){
			
			super.getHandler().setConnectionState(state);
		
		}else{
			
			throw new IllegalStateException("State " +state.toString()+ " does not reflect access decision");
		
		}
		
	}

}
