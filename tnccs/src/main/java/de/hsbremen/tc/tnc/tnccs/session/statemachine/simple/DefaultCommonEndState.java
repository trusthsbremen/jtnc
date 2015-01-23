package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.End;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;

class DefaultCommonEndState extends AbstractState implements End {

	private boolean server;
	
	DefaultCommonEndState(boolean server, StateHelper<? extends TnccsContentHandler> factory){
		super(factory.getHandler());
		this.server = server;
	}
	
	@Override
	public State getProcessorState(TnccsBatch result) {
		// if getProcesor is invoked, the EndState was already handled once, so set the successor
		// to prevent the EndState from handling a message with the handle method.
		
		super.setSuccessor(this);
		return this;
	}

	@Override
	public TnccsBatch collect() {
		TnccsBatch b = null;
		
		if(super.getSuccessor() == null){
		
			b = StateUtil.createCloseBatch(server);
			super.setSuccessor(this);
					
		}
		
		return b;
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
			super.getHandler().dumpMessages(b.getMessages());
		}
		
		if(batchContainer.getExceptions() != null){
			super.getHandler().dumpExceptions(batchContainer.getExceptions());
		}
	}

}
