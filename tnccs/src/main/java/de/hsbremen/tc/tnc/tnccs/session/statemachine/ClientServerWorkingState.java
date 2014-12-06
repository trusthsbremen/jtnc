package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

public class ClientServerWorkingState extends AbstractState implements ServerWorking {

	
	public ClientServerWorkingState(TnccsContentHandler handler){
		super(handler);
	}
	
	@Override
	public State getProcessorState(TnccsBatch result) {

		if(result != null && result instanceof PbBatch){
			PbBatch b = (PbBatch) result;
			
			if(b.getHeader().getType().equals(PbBatchTypeEnum.SDATA)){	
		
				return DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.CLIENT_WORKING, super.getHandler());
			}
		
			if(b.getHeader().getType().equals(PbBatchTypeEnum.RESULT)){
				
				return DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.DECIDED, super.getHandler());
			}

			if(b.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){
			
				return DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.END, super.getHandler());
			}
			
			if(b.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)){
				super.setSuccessor(null);
				return this;
			}
			
		}
			
		return DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.ERROR, super.getHandler());

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
