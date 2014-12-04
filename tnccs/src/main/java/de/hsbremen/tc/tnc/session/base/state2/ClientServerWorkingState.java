package de.hsbremen.tc.tnc.session.base.state2;

import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;

public class ClientServerWorkingState extends AbstractClientState implements ServerWorking{

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientServerWorkingState.class);
	
	public ClientServerWorkingState(TnccsContentHandler handler) {
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

						
					context.setState(new ClientClientWorkingState(super.getHandler()));
					b = context.getState().handle(context, batchContainer);

				}else if(current.getHeader().getType().equals(PbBatchTypeEnum.RESULT)){
					
					this.handleResult(batchContainer);
					context.setState(new ClientDecidedState(super.getHandler()));

				}else if(current.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){
					
					context.setState(ClientStateHelper.handleClose(super.getHandler(), context, batchContainer));
				
				}else if(current.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)){	
					// this is redundant and can be ignored, wait for next message	
					context.setState(new ClientServerWorkingState(super.getHandler()));	
					
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
	
	private void handleResult(TnccsBatchContainer batchContainer) {
		PbBatch b = (PbBatch) batchContainer.getResult();
		if(b.getMessages() != null){
			super.getHandler().handleMessages(b.getMessages());
		}
		
		if(batchContainer.getExceptions() != null){
			super.getHandler().handleExceptions(batchContainer.getExceptions());
		}
		
		// important - hopefully this updates to decided, if not who knows what happens
		super.getHandler().setConnectionState(super.getHandler().getAccessDecision());
	}
}
