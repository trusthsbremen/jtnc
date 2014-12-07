package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.AbstractState;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.Decided;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

class DefaultServerDecidedState extends AbstractState implements Decided {
	
	private StateHelper<TncsContentHandler> factory;
	
	DefaultServerDecidedState(StateHelper<TncsContentHandler> factory) {
		super(factory.getHandler());
		this.factory = factory;
	}

	@Override
	public State getProcessorState(TnccsBatch result) {
		if(result != null && result instanceof PbBatch){
			PbBatch b = (PbBatch) result;
			
			if(b.getHeader().getType().equals(PbBatchTypeEnum.CRETRY)){	
		
				return this.factory.createState(TnccsStateEnum.SERVER_WORKING);
			}

			if(b.getHeader().getType().equals(PbBatchTypeEnum.CLOSE)){
			
				return this.factory.createState(TnccsStateEnum.END);
			}
			
		}
			
		return this.factory.createState(TnccsStateEnum.ERROR);
	}
}
