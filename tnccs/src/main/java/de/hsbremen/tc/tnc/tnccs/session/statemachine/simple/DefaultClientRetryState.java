package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatchFactoryIetf;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Retry;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

class DefaultClientRetryState extends AbstractState implements Retry {

	private StateHelper<TnccContentHandler> factory;
	
	DefaultClientRetryState(StateHelper<TnccContentHandler> factory){
		super(factory.getHandler());
		this.factory = factory;
	}
	
	@Override
	public TnccsBatch collect() {
		TnccsBatch b = null;
		
		List<TnccsMessage> messages  = super.getHandler().collectMessages();
		try{
				
			b = this.createServerBatch(messages);
			super.setSuccessor(this.factory.createState(TnccsStateEnum.SERVER_WORKING));
				
		}catch(ValidationException e){
				
			TnccsMessage error = StateUtil.createLocalError();
			b = StateUtil.createCloseBatch(false,error);
			super.setSuccessor(this.factory.createState(TnccsStateEnum.END));
	
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
