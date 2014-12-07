package de.hsbremen.tc.tnc.tnccs.session.statemachine.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.message.handler.TncsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.State;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

public class DefaultServerStateFactory implements StateHelper<TncsContentHandler>{

	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultServerStateFactory.class);
	private final TncsContentHandler contentHandler;
	
	public DefaultServerStateFactory(TncsContentHandler contentHandler){
		if(contentHandler == null){
			throw new NullPointerException("Content handler cannot be null.");
		}
		this.contentHandler = contentHandler;
	}

	
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.statemachine.StateHelper#getHander()
	 */
	@Override
	public TncsContentHandler getHandler() {
		return this.contentHandler;
	}



	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.statemachine.StateFactory#createState(de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum, de.hsbremen.tc.tnc.tnccs.session.base.state.TnccsContentHandler)
	 */
	@Override
	public State createState(TnccsStateEnum id){
		
		State t = null;
		switch(id){
		case CLIENT_WORKING:
			t = new DefaultServerClientWorkingState(this);
			break;
		case DECIDED:
			t = new DefaultServerDecidedState(this);
			break;
		case END:
			t = new DefaultCommonEndState(true, this.contentHandler);
			break;
		case ERROR:
			t = new DefaultCommonErrorState(true, this);
			break;
		case INIT:
			t = new DefaultServerInitState(this);
			break;
		case RETRY:
			t = new DefaultServerRetryState(this);
			break;
		case SERVER_WORKING:
			t = new DefaultServerServerWorkingState(this);
			break;
		}
		if(t != null){
			LOGGER.debug(t.getClass().getCanonicalName() + " created.");
			return t;
		}else{
			throw new IllegalArgumentException("The implementation of the state with id " + id.value() +" is unknown.");
		}
	}	
}
