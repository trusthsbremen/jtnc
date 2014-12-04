package de.hsbremen.tc.tnc.session.base.state2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.tnccs.batch.TnccsBatch;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;


public class EndState implements End {

	protected static final Logger LOGGER = LoggerFactory.getLogger(EndState.class);

	private final TnccsContentHandler handler;
	
	public EndState(TnccsContentHandler handler) {
		this.handler = handler;
	}

	
	@Override
	public TnccsBatch handle(StateContext context) {
		LOGGER.info("Session end state is reached.");
		handler.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE);
		return null;
	}

	@Override
	public TnccsBatch handle(StateContext context, TnccsBatchContainer batch) {
		LOGGER.info("Session end state is reached.");
		return null;
	}

}
