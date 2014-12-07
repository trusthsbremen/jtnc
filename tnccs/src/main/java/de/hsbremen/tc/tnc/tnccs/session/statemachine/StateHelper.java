package de.hsbremen.tc.tnc.tnccs.session.statemachine;

import de.hsbremen.tc.tnc.tnccs.message.handler.TnccsContentHandler;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.enums.TnccsStateEnum;

public interface StateHelper<T extends TnccsContentHandler> {

	public abstract T getHandler();
	
	public abstract State createState(TnccsStateEnum id);

}