package de.hsbremen.tc.tnc.newp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class DefaultTnccHandler implements TnccHandler{

	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultTnccHandler.class);
	private TncConnectionState connectionState; 
	
	
	@Override
	public void setConnectionState(TncConnectionState imConnectionState) {
		LOGGER.debug("Connection state has changed to: " + connectionState.toString());
		this.connectionState = imConnectionState;
		
	}

	@Override
	public void requestMessages(TnccsSessionContext context) {
		// TODO Auto-generated method stub
	}

	@Override
	public void forwardMessage(TnccsSessionContext context,
			TnccsMessageValue value) {
		// TODO Auto-generated method stub
	}

}
