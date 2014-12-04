package de.hsbremen.tc.tnc.session.base.state2;

import java.util.List;

import org.ietf.nea.pb.batch.PbBatchFactoryIetf;

import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.session.base.state2.StateContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class ClientRetryState extends AbstractClientState implements Retry {
	
	public ClientRetryState(TnccsContentHandler handler) {
		super(handler);
	}

	
	@Override
	public TnccsBatch handle(StateContext context) {

		TnccsBatch b = null;
		
		List<TnccsMessage> messages  = super.getHandler().collectMessages();
		try{
				
			b = this.createServerBatch(messages);
			context.setState(new ClientServerWorkingState(super.getHandler()));
				
		}catch(ValidationException e){
				
			TnccsMessage error = ClientStateHelper.createLocalError();
			b = ClientStateHelper.createCloseBatch(error);
			context.setState(new EndState(super.getHandler()));
			context.getState().handle(context);
		}
			
		
		return b;
	}


	private TnccsBatch createServerBatch(List<TnccsMessage> messages) throws ValidationException {
		return PbBatchFactoryIetf.createClientData(messages);
	}

}