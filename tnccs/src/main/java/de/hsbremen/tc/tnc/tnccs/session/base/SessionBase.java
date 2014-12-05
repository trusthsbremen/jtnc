package de.hsbremen.tc.tnc.tnccs.session.base;

import de.hsbremen.tc.tnc.attribute.Attributed;
import de.hsbremen.tc.tnc.tnccs.session.base.state.StateMachine;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsInputChannel;
import de.hsbremen.tc.tnc.tnccs.session.connection.TnccsOutputChannel;

public interface SessionBase {

	public abstract void registerStatemachine(StateMachine m);

	public abstract void registerInput(TnccsInputChannel in);

	public abstract void registerOutput(TnccsOutputChannel out);

	public abstract void start(boolean selfInitiated);
	
	public abstract void close();
	
	public Attributed getAttributes();


}