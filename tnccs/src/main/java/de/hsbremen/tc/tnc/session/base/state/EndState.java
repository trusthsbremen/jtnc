package de.hsbremen.tc.tnc.session.base.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.DefaultTncConnectionStateEnum;
import de.hsbremen.tc.tnc.session.base.StateContext;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsBatchContainer;


public class EndState implements End {

	protected static final Logger LOGGER = LoggerFactory.getLogger(EndState.class);

	@Override
	public StateResult handle(StateContext ctx) {
		LOGGER.info("Session end state is reached.");
		ctx.setConnectionState(DefaultTncConnectionStateEnum.TNC_CONNECTION_STATE_DELETE);
		return new DefaultStateResult(this, null);
	}

	@Override
	public StateResult handle(StateContext ctx, TnccsBatchContainer batch) {
		LOGGER.info("Session end state is reached.");
		return new DefaultStateResult(this, null);
	}

}
