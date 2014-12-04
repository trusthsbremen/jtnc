package de.hsbremen.tc.tnc.newp.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.connection.TncConnectionState;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class DefaultTnccHandler implements TnccHandler{

	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultTnccHandler.class);

	@Override
	public TncConnectionState getAccessDecision() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setConnectionState(TncConnectionState state) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<TnccsMessage> requestMessages() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<TnccsMessage> forwardMessage(TnccsMessageValue value) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
}
