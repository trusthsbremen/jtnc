package de.hsbremen.tc.tnc.session.base.state;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchFactoryIetf;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.session.base.StateContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;

public class ClientServerWorkingState extends AbstractClientState implements ServerWorking{

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientServerWorkingState.class);

	@Override
	public StateResult handle(StateContext ctx, TnccsBatchContainer batch) {
		SessionState successor = new EndState();
		
		TnccsBatchContainer batchContainer = batch;
		
		TnccsBatch b = null;
		
		if(batchContainer.getResult() == null){
			LOGGER.error("Batch is NULL, transitioning to end state, while trying to handle exceptions." ); 
			if(batchContainer.getExceptions() != null){
		
				List<TnccsMessage> messages = ctx.handleExceptions(batchContainer.getExceptions());
				b = ClientStateHelper.createCloseBatch(messages.toArray(new TnccsMessage[messages.size()]));
				successor = new EndState();
				successor.handle(ctx);
				
			}else{
				
				b = ClientStateHelper.createCloseBatch();
				successor = new EndState();
				successor.handle(ctx);
				
			}
		
		}else{
		
			if(batchContainer.getResult() instanceof PbBatch){
				PbBatch current = (PbBatch) batchContainer.getResult();
				if(current.getHeader().getType().equals(PbBatchTypeEnum.SDATA)){	
					
					try {
						
						b = this.handleSdata(ctx, batchContainer);
						successor = new ClientServerWorkingState();
						
					} catch (ValidationException e) {
						TnccsMessage error = ClientStateHelper.createLocalError();
						b = ClientStateHelper.createCloseBatch(error);
						successor = new EndState();
						successor.handle(ctx);
					}
					
					
				}else if(current.getHeader().getType().equals(PbBatchTypeEnum.RESULT)){
					
					this.handleResult(ctx, batchContainer);
					successor = new ClientDecidedState();

				}else if(current.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){
					
					successor = ClientStateHelper.handleClose(ctx,batchContainer);
				
				}else if(current.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)){	
					// this is redundant and can be ignored, wait for next message	
					successor = new ClientServerWorkingState();	
					
				}else{
					
					TnccsMessage error = ClientStateHelper.createUnexpectedStateError();
					b = ClientStateHelper.createCloseBatch(error);
					successor = new EndState();
					successor.handle(ctx);
					
				}
			}else{
				
				TnccsMessage error = ClientStateHelper.createUnsupportedVersionError(batchContainer.getResult().getHeader().getVersion(), (short)2, (short)2);
				b = ClientStateHelper.createCloseBatch(error);
				successor = new EndState();
				successor.handle(ctx);
				
			}
		}
		
		return new DefaultStateResult(successor, b);
	}
	
	private TnccsBatch handleSdata(StateContext ctx, TnccsBatchContainer batchContainer) throws ValidationException{
		
		PbBatch current = (PbBatch) batchContainer.getResult();
		
		List<? extends TnccsMessage> request = current.getMessages();
		List<TnccsMessage> response = new LinkedList<>();
		
		if( request != null ){
			
			List<TnccsMessage> msgs = ctx.handleMessages(request);
			if(msgs != null){
				response.addAll(msgs);
			}
			
		}
		
		if(batchContainer.getExceptions() != null){
			List<TnccsMessage> msgs = ctx.handleExceptions(batchContainer.getExceptions());
			if(msgs != null){
				response.addAll(msgs);
			}
		}
		

		TnccsBatch b = this.createServerBatch(response);		
		
		return b;
	}
	
	private void handleResult(StateContext ctx, TnccsBatchContainer batchContainer) {
		PbBatch b = (PbBatch) batchContainer.getResult();
		if(b.getMessages() != null){
			ctx.handleMessages(b.getMessages());
		}
		
		if(batchContainer.getExceptions() != null){
			ctx.handleExceptions(batchContainer.getExceptions());
		}
		
		// important - hopefully this updates to decided, if not who knows what happens
		ctx.setConnectionState(ctx.getConnectionStateUpdate());
	}
	
	private TnccsBatch createServerBatch(List<TnccsMessage> messages) throws ValidationException {
		return PbBatchFactoryIetf.createClientData((messages != null) ? messages : new ArrayList<TnccsMessage>(0));
	}
}
