package de.hsbremen.tc.tnc.session.base;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.session.base.state.StateMachine;
import de.hsbremen.tc.tnc.session.connection.TnccsInputChannel;
import de.hsbremen.tc.tnc.session.connection.TnccsOutputChannel;

public interface SessionBase {

	public abstract void registerStatemachine(StateMachine m);

	public abstract void registerInput(TnccsInputChannel in);

	public abstract void registerOutput(TnccsOutputChannel out);

	public abstract void start(boolean selfInitiated);
	
	public abstract void close();
	
	public Attributed getAttributes();


}