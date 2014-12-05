package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.session.base.state.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

public class ClientClientWorkingState extends AbstractState implements ClientWorking {

	
	public ClientClientWorkingState(TnccsContentHandler handler){
		super(handler);
	}

	@Override
	public TnccsBatch handle(TnccsBatchContainer batchContainer) {
		
		TnccsBatch b = null;
		
		if(batchContainer.getResult() == null){
			throw new NullPointerException("Batch cannot be null. The state transitions seem corrupted."); 
		
		}else{
		
			if(batchContainer.getResult() instanceof PbBatch){
				PbBatch current = (PbBatch) batchContainer.getResult();
				
				if(current.getHeader().getType().equals(PbBatchTypeEnum.SDATA)){	
					
					try {
						b = this.handleSdata(batchContainer);
						super.setSuccessor(DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.SERVER_WORKING, super.getHandler()));
						
					} catch (ValidationException e) {
						TnccsMessage error = ClientStateHelper.createLocalError();
						b = ClientStateHelper.createCloseBatch(error);
						super.setSuccessor(DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.END, super.getHandler()));
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

	@Override
	public State getConclusiveState() {
		State s = super.getSuccessor();
		super.setSuccessor(null);
		
		return s;
	}

	private TnccsBatch handleSdata(TnccsBatchContainer batchContainer) throws ValidationException{
		
		PbBatch current = (PbBatch) batchContainer.getResult();
		
		List<? extends TnccsMessage> request = current.getMessages();
		List<TnccsMessage> response = new LinkedList<>();
		
		if( request != null ){
			
			List<TnccsMessage> msgs =super.getHandler().handleMessages(request);
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

		TnccsBatch b = this.createServerBatch(response);		
		
		return b;
	}
	
	private TnccsBatch createServerBatch(List<TnccsMessage> messages) throws ValidationException {
		return PbBatchFactoryIetf.createClientData((messages != null) ? messages : new ArrayList<TnccsMessage>(0));
	}
	
}
