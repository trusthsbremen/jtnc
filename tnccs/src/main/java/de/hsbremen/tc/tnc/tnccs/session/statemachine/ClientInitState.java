package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatchFactoryIetf;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.session.base.state.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

public class ClientInitState extends AbstractState implements Init {
	
	public ClientInitState(TnccsContentHandler handler){
		super(handler);
	}

	@Override
	public TnccsBatch collect() {
		TnccsBatch b = null;
		
		List<TnccsMessage> messages  = super.getHandler().collectMessages();
		try{
				
			b = this.createServerBatch(messages);
			super.setSuccessor(DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.SERVER_WORKING, super.getHandler()));
				
		}catch(ValidationException e){
				
			TnccsMessage error = ClientStateHelper.createLocalError();
			b = ClientStateHelper.createCloseBatch(error);
			super.setSuccessor(DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.END, super.getHandler()));
	
		}
		
		return b;
	}

	@Override
	public State getConclusiveState() {
		State s = super.getSuccessor();
		super.setSuccessor(null);
		
		return s;
	}
	

	private TnccsBatch createServerBatch(List<TnccsMessage> messages) throws ValidationException {
		return PbBatchFactoryIetf.createClientData((messages != null) ? messages : new ArrayList<TnccsMessage>(0));
	}
}
