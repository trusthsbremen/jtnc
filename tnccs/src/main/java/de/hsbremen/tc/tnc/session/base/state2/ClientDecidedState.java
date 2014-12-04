package de.hsbremen.tc.tnc.session.base.state2;

import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.session.base.state2.StateContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;

public class ClientDecidedState extends AbstractClientState implements Decided{

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientDecidedState.class);
	
	public ClientDecidedState(TnccsContentHandler handler) {
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
				
				if(current.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){
					
					context.setState(ClientStateHelper.handleClose(super.getHandler(), context ,batchContainer));
				
				}else if(current.getHeader().getType().equals(PbBatchTypeEnum.SRETRY)){
					
					this.handleRetry(batchContainer);
					// if this notification comes early the other parts would already engage in a new handshake.
					super.getHandler().setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);
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
	
	private void handleRetry(TnccsBatchContainer batchContainer) {
		PbBatch b = (PbBatch) batchContainer.getResult();
		if(b.getMessages() != null){
			super.getHandler().handleMessages(b.getMessages());
		}
		
		if(batchContainer.getExceptions() != null){
			super.getHandler().handleExceptions(batchContainer.getExceptions());
		}
	}
	
}
