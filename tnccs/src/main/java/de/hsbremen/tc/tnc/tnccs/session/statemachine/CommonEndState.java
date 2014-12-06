package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;

public class CommonEndState extends AbstractState implements End {

	public CommonEndState(TnccsContentHandler handler){
		super(handler);
	}
	
	@Override
	public State getProcessorState(TnccsBatch result) {
		// if getProcesor is invoked, the EndState was already handled once, so set the successor
		// to prevent the EndState from handling a message with the handle method.
		
		super.setSuccessor(this);
		return this;
	}

	@Override
	public TnccsBatch handle(TnccsBatchContainer batchContainer) {
		TnccsBatch b = null;
		if(super.getSuccessor() == null){
			
			if(batchContainer.getResult() == null){
				throw new NullPointerException("Batch cannot be null. The state transitions seem corrupted."); 
			
			}else{
			
				if(batchContainer.getResult() instanceof PbBatch){
					PbBatch current = (PbBatch) batchContainer.getResult();
					
					if(current.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){	
						this.handleClose(batchContainer);
						super.setSuccessor(this);
					}else{
						
						throw new IllegalArgumentException("Batch cannot be of type " + current.getHeader().getType().toString() + ". The state transitions seem corrupted.");
					
					}
				}else{
					
					throw new IllegalArgumentException("Batch must be an instance of " + PbBatch.class.getCanonicalName() + ". The state transitions seem corrupted.");

				}
			}
		}
			
		return b;
		
	}

	@Override
	public State getConclusiveState() {
		// the end is the end
		return this;
	}

	private void handleClose(TnccsBatchContainer batchContainer) {
		PbBatch b = (PbBatch) batchContainer.getResult();
		if(b.getMessages() != null){
			super.getHandler().handleMessages(b.getMessages());
		}
		
		if(batchContainer.getExceptions() != null){
			super.getHandler().handleExceptions(batchContainer.getExceptions());
		}
	}
}
