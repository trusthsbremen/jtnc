package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.ServerWorking;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

class DefaultClientServerWorkingState extends AbstractState implements ServerWorking {

	private StateHelper<TnccContentHandler> factory;
	
	DefaultClientServerWorkingState(StateHelper<TnccContentHandler> factory){
		super(factory.getHandler());
		this.factory = factory;
	}
	
	@Override
	public State getProcessorState(TnccsBatch result) {

		if(result != null && result instanceof PbBatch){
			PbBatch b = (PbBatch) result;
			
			if(b.getHeader().getType().equals(PbBatchTypeEnum.SDATA)){	
		
				return this.factory.createState(TnccsStateEnum.CLIENT_WORKING);
			}
		
			if(b.getHeader().getType().equals(PbBatchTypeEnum.RESULT)){
				
				return this.factory.createState(TnccsStateEnum.DECIDED);
			}

			if(b.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){
			
				return this.factory.createState(TnccsStateEnum.END);
			}
			
			if(b.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)){
				super.setSuccessor(null);
				return this;
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
				
				if(current.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)){	
					// this is redundant and can be ignored, wait for next message
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

}
