package de.hsbremen.tc.tnc.session.base.state;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatchFactoryIetf;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.session.base.StateContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class ClientInitState extends AbstractClientState implements Init{
	
	@Override
	public StateResult handle(StateContext ctx) {

		SessionState successor = new EndState();
		
		ctx.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_CREATE);
		ctx.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_HANDSHAKE);
		
		TnccsBatch b = null;
		
		List<TnccsMessage> messages  = ctx.collectMessages();
		try{
				
			b = this.createServerBatch(messages);
			successor = new ClientServerWorkingState();
				
		}catch(ValidationException e){
				
			TnccsMessage error = ClientStateHelper.createLocalError();
			b = ClientStateHelper.createCloseBatch(error);
			successor = new EndState();
			successor.handle(ctx);
		}
		
		return new DefaultStateResult(successor, b);
	}


	private TnccsBatch createServerBatch(List<TnccsMessage> messages) throws ValidationException {
		return PbBatchFactoryIetf.createClientData((messages != null) ? messages : new ArrayList<TnccsMessage>(0));
	}


}
