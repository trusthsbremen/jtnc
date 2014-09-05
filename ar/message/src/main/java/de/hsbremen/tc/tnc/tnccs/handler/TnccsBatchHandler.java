package de.hsbremen.tc.tnc.tnccs.handler;

import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;


public interface TnccsBatchHandler {

	public void handleBatch(TnccsBatch b);
	
	boolean batchIsValid(TnccsBatch b);
}
