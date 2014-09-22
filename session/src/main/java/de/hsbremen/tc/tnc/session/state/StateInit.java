package de.hsbremen.tc.tnc.session.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.session.context.SessionContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class StateInit extends AbstractPbState {

	private static final Logger LOGGER = LoggerFactory.getLogger(StateInit.class);

	@Override
	protected SessionState handleServer(SessionContext context)
			throws ConnectionException, SerializationException {
		
		TnccsBatch b = createServerDataBatch();
		
		try{
			context.sendBatch(b);
		}catch(ConnectionException | SerializationException e){
			LOGGER.error("Exception occured while trying to send a batch. Exception will be handled by super class.");
			throw e;
		}
		
		return new StateClientWorking();
	}

	@Override
	protected SessionState handleClient(SessionContext context)
			throws ConnectionException, SerializationException {
		
		TnccsBatch b = createClientDataBatch();
		
		try{
			context.sendBatch(b);
		}catch(ConnectionException | SerializationException e){
			LOGGER.error("Exception occured while trying to send a batch. Exception will be handled by super class.");
			throw e;
		}
		
		return new StateServerWorking();
	}
	
	private TnccsBatch createServerDataBatch(){
		
		return null;
	}

	private TnccsBatch createClientDataBatch(){
		
		return null;
	}
}
