package de.hsbremen.tc.tnc.session.state;

import org.ietf.nea.pb.batch.PbBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.session.context.SessionContext;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.transport.exception.ConnectionException;

public class StateClientWorking extends AbstractPbState{

	private static final Logger LOGGER = LoggerFactory.getLogger(StateClientWorking.class);
	
	@Override
	protected SessionState handleAsServer(SessionContext context)
			throws ConnectionException, SerializationException {

		TnccsBatch b = null;
		try{
			b = context.receiveBatch();
		}catch(ConnectionException | SerializationException e){
			LOGGER.error("Exception occured while trying to receive a batch. Exception will be handled by super class.");
			throw e;
		}
		
		if(b != null && b instanceof PbBatch){
				
		}

		return ;
	}

	@Override
	protected SessionState handleAsClient(SessionContext context)
			throws ConnectionException, SerializationException {

		

		return null;
	}
	
}
