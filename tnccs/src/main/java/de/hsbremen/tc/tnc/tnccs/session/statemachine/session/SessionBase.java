package de.hsbremen.tc.tnc.tnccs.session.statemachine.session;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsInputChannel;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsOutputChannel;
import de.hsbremen.tc.tnc.tnccs.session.statemachine.StateMachine;

public interface SessionBase {

	public abstract void registerStatemachine(StateMachine m);

	public abstract void registerInput(TnccsInputChannel in);

	public abstract void registerOutput(TnccsOutputChannel out);

	public abstract void start(boolean selfInitiated);
	
	public abstract void close();
	
	public boolean isClosed();
	
	public Attributed getAttributes();


}