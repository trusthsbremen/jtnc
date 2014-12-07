package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import java.util.ArrayList;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatchFactoryIetf;

import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Init;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

class DefaultServerInitState extends AbstractState implements Init {
	
	private StateHelper<TncsContentHandler> factory;
	
	DefaultServerInitState(StateHelper<TncsContentHandler> factory){
		super(factory.getHandler());
		this.factory = factory;
	}

	@Override
	public TnccsBatch collect() {
		TnccsBatch b = null;
		
		List<TnccsMessage> messages  = super.getHandler().collectMessages();
		try{
				
			b = this.createClientBatch(messages);
			super.setSuccessor(this.factory.createState(TnccsStateEnum.CLIENT_WORKING));
				
		}catch(ValidationException e){
				
			TnccsMessage error = StateUtil.createLocalError();
			b = StateUtil.createCloseBatch(true, error);
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
	

	private TnccsBatch createClientBatch(List<TnccsMessage> messages) throws ValidationException {
		return PbBatchFactoryIetf.createServerData((messages != null) ? messages : new ArrayList<TnccsMessage>(0));
	}
}
