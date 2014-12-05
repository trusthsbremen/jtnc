package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import de.hsbremen.tc.tnc.tnccs.session.base.state.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

public class DefaultClientStateFactory implements StateFactory{

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
		
		switch(id){
		case CLIENT_WORKING:
			return new ClientClientWorkingState(contentHandler);
		case DECIDED:
			return new ClientDecidedState(contentHandler);
		case END:
			return new CommonEndState(contentHandler);
		case ERROR:
			return new CommonErrorState(contentHandler);
		case INIT:
			return new ClientInitState(contentHandler);
		case RETRY:
			return new ClientRetryState(contentHandler);
		case SERVER_WORKING:
			return new ClientServerWorkingState(contentHandler);
		}
		throw new IllegalArgumentException("The implementation of the state with id " + id.value() +" is unknown.");
	}
	
	
	
	
}
