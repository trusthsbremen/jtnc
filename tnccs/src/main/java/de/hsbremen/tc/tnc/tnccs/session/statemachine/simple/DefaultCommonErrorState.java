package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Error;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

class DefaultCommonErrorState extends AbstractState implements Error{

	private StateHelper<? extends TnccsContentHandler> factory;
	private boolean server;
	
	DefaultCommonErrorState(boolean server, StateHelper<? extends TnccsContentHandler> factory){
		super(factory.getHandler());
		this.factory = factory;
		this.server = server;
	}

	@Override
	public TnccsBatch handle(TnccsBatchContainer batchContainer) {
		
		TnccsBatch b = null;
		
		if(batchContainer.getResult() == null){
		
			if(batchContainer.getExceptions() != null){
		
				List<TnccsMessage> messages =super.getHandler().handleExceptions(batchContainer.getExceptions());
				b = StateUtil.createCloseBatch(server,messages.toArray(new TnccsMessage[messages.size()]));
				super.setSuccessor(this.factory.createState(TnccsStateEnum.END));
				
			}else{
				
				b = StateUtil.createCloseBatch(server);
				super.setSuccessor(this.factory.createState(TnccsStateEnum.END));
				
			}
		}else{
		
			if(batchContainer.getResult() instanceof PbBatch){

				TnccsMessage error = StateUtil.createUnexpectedStateError();
				b =  StateUtil.createCloseBatch(server, error);
				super.setSuccessor(this.factory.createState(TnccsStateEnum.END));
				
			}else{
			
				TnccsMessage error = StateUtil.createUnsupportedVersionError(batchContainer.getResult().getHeader().getVersion(), (short)2, (short)2);
				b = StateUtil.createCloseBatch(server,error);
				super.setSuccessor(this.factory.createState(TnccsStateEnum.END));
			
			}
		}
		
		return b;

	}

	@Override
	public State getConclusiveState() {
		State s = super.getSuccessor();
		super.setSuccessor(null);
		
		return s;
	}

	
	
}
