package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

public class DefaultClientStateFactory implements StateFactory{

	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultClientStateFactory.class);
	
	private static class Singleton {
		private static final StateFactory INSTANCE = new DefaultClientStateFactory();
	}
	
	public static StateFactory getInstance(){
		return Singleton.INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see de.hsbremen.tc.tnc.tnccs.session.statemachine.StateFactory#createState(de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum, de.hsbremen.tc.tnc.tnccs.session.base.state.TnccsContentHandler)
	 */
	@Override
	public State createState(TnccsStateEnum id, TnccsContentHandler contentHandler){
		if(contentHandler == null){
			throw new NullPointerException("Content handler cannot be null.");
		}
		State t = null;
		switch(id){
		case CLIENT_WORKING:
			t = new ClientClientWorkingState(contentHandler);
			break;
		case DECIDED:
			t = new ClientDecidedState(contentHandler);
			break;
		case END:
			t = new CommonEndState(contentHandler);
			break;
		case ERROR:
			t = new CommonErrorState(contentHandler);
			break;
		case INIT:
			t = new ClientInitState(contentHandler);
			break;
		case RETRY:
			t = new ClientRetryState(contentHandler);
			break;
		case SERVER_WORKING:
			t = new ClientServerWorkingState(contentHandler);
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
