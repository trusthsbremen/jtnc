package de.hsbremen.tc.tnc.session.base.state;

import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.session.base.StateContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;

public class ClientDecidedState extends AbstractClientState implements Decided{

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientDecidedState.class);


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
				
				if(current.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){
					
					successor = ClientStateHelper.handleClose(ctx,batchContainer);
				
				}else if(current.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)){
					
					this.handleRetry(ctx, batchContainer);
					// if this notification comes early the other parts would already engage in a new handshake.
					ctx.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);
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
	
	private void handleRetry(StateContext ctx, TnccsBatchContainer batchContainer) {
		PbBatch b = (PbBatch) batchContainer.getResult();
		if(b.getMessages() != null){
			ctx.handleMessages(b.getMessages());
		}
		
		if(batchContainer.getExceptions() != null){
			ctx.handleExceptions(batchContainer.getExceptions());
		}
	}
	
}
