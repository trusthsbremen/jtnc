package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.tnccs.session.base.state.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

public class CommonErrorState extends AbstractState implements Error{

	public CommonErrorState(TnccsContentHandler handler){
		super(handler);
	}

	@Override
	public TnccsBatch handle(TnccsBatchContainer batchContainer) {
		
		TnccsBatch b = null;
		
		if(batchContainer.getResult() == null){
		
			if(batchContainer.getExceptions() != null){
		
				List<TnccsMessage> messages =super.getHandler().handleExceptions(batchContainer.getExceptions());
				b = ClientStateHelper.createCloseBatch(messages.toArray(new TnccsMessage[messages.size()]));
				super.setSuccessor(DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.END, super.getHandler()));
				
			}else{
				
				b = ClientStateHelper.createCloseBatch();
				super.setSuccessor(DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.END, super.getHandler()));
				
			}
		}else{
		
			if(batchContainer.getResult() instanceof PbBatch){

				TnccsMessage error = ClientStateHelper.createUnexpectedStateError();
				b = ClientStateHelper.createCloseBatch(error);
				super.setSuccessor(DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.END, super.getHandler()));
				
			}else{
			
				TnccsMessage error = ClientStateHelper.createUnsupportedVersionError(batchContainer.getResult().getHeader().getVersion(), (short)2, (short)2);
				b = ClientStateHelper.createCloseBatch(error);
				super.setSuccessor(DefaultClientStateFactory.getInstance().createState(TnccsStateEnum.END, super.getHandler()));
			
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
