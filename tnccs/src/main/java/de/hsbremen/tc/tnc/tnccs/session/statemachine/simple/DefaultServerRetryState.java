package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import java.util.ArrayList;

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

class DefaultServerRetryState extends AbstractState implements Init {
	
	private StateHelper<TncsContentHandler> factory;
	
	DefaultServerRetryState(StateHelper<TncsContentHandler> factory){
		super(factory.getHandler());
		this.factory = factory;
	}

	@Override
	public TnccsBatch collect() {
		TnccsBatch b = null;
		
		try{
				
			b = this.createServerRetry();
			super.setSuccessor(this.factory.createState(TnccsStateEnum.SERVER_WORKING));
				
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
	

	private TnccsBatch createServerRetry() throws ValidationException {
		return PbBatchFactoryIetf.createServerRetry(new ArrayList<TnccsMessage>());
	}
}
