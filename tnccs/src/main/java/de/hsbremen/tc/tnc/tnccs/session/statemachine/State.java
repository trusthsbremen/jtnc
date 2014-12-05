package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import de.hsbremen.tc.tnc.message.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsBatchContainer;

public interface State {

	public State getProcessorState(TnccsBatch result);
	public TnccsBatch collect();
	public TnccsBatch handle(TnccsBatchContainer batchContainer);
	public State getConclusiveState();
	
}
