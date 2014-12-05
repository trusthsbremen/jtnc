package de.hsbremen.tc.tnc.tnccs.session.base.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.session.base.state.StateContext;

public class ClientClientWorkingState extends AbstractClientState implements ServerWorking{

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientClientWorkingState.class);
	
	public ClientClientWorkingState(TnccsContentHandler handler) {
		super(handler);
	}

	
	@Override
	public TnccsBatch handle(StateContext context, TnccsBatchContainer batch) {
		
		TnccsBatchContainer batchContainer = batch;
		
		TnccsBatch b = null;
		
		if(batchContainer.getResult() == null){
			LOGGER.error("Batch is NULL, transitioning to end state, while trying to handle exceptions." ); 
			if(batchContainer.getExceptions() != null){
		
				List<TnccsMessage> messages = super.getHandler().handleExceptions(batchContainer.getExceptions());
				b = ClientStateHelper.createCloseBatch(messages.toArray(new TnccsMessage[messages.size()]));
				context.setState(new EndState(super.getHandler()));
				context.getState().handle(context);
				
			}else{
				
				b = ClientStateHelper.createCloseBatch();
				context.setState(new EndState(super.getHandler()));
				context.getState().handle(context);
				
			}
		
		}else{
		
			if(batchContainer.getResult() instanceof PbBatch){
				PbBatch current = (PbBatch) batchContainer.getResult();
				if(current.getHeader().getType().equals(PbBatchTypeEnum.SDATA)){	
					
					try {
						
						b = this.handleSdata(batchContainer);
						context.setState(new ClientServerWorkingState(super.getHandler()));
						
					} catch (ValidationException e) {
						TnccsMessage error = ClientStateHelper.createLocalError();
						b = ClientStateHelper.createCloseBatch(error);
						context.setState(new EndState(super.getHandler()));
						context.getState().handle(context);
					}

				}else if(current.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){
					
					context.setState(ClientStateHelper.handleClose(super.getHandler(), context, batchContainer));
				
				}else{
					
					TnccsMessage error = ClientStateHelper.createUnexpectedStateError();
					b = ClientStateHelper.createCloseBatch(error);
					context.setState(new EndState(super.getHandler()));
					context.getState().handle(context);
					
				}
			}else{
				
				TnccsMessage error = ClientStateHelper.createUnsupportedVersionError(batchContainer.getResult().getHeader().getVersion(), (short)2, (short)2);
				b = ClientStateHelper.createCloseBatch(error);
				context.setState(new EndState(super.getHandler()));
				context.getState().handle(context);
				
			}
		}
		
		return b;
	}
	
	private TnccsBatch handleSdata(TnccsBatchContainer batchContainer) throws ValidationException{
		
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
			List<TnccsMessage> msgs = super.getHandler().handleExceptions(batchContainer.getExceptions());
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
