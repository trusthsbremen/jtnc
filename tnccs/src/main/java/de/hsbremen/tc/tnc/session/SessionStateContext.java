package de.hsbremen.tc.tnc.session;

import de.hsbremen.tc.tnc.exception.HandlingException;
import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public interface SessionStateContext {

	// TODO throws exception if not working
		abstract TnccsBatchContainer receiveBatch() throws SerializationException, ConnectionException, ValidationException;

		// TODO throws exception if not working
		abstract void sendBatch(TnccsBatch b) throws SerializationException, ConnectionException;
		
		abstract void handleBatch(TnccsBatch b) throws HandlingException;
}
