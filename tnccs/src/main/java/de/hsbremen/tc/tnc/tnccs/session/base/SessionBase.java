package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.exception.ComprehensibleException;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;
import de.hsbremen.tc.tnc.transport.TransportConnection;

public interface SessionBase {

	public abstract void registerStatemachine(StateMachine machine);
	
	public abstract void registerConnection(TransportConnection connection);
	
	public abstract void start(boolean selfInitiated);
	
	public abstract void handle(ComprehensibleException e);
	
	public abstract void close();
	
	public abstract boolean isClosed();
	
	public abstract Attributed getAttributes();



	


}