package de.hsbremen.tc.tnc.session.base;

import de.hsbremen.tc.tnc.session.connection.BatchSender;
import de.hsbremen.tc.tnc.session.connection.TnccsInputChannel;

public interface SessionBase {

	public abstract void registerStatemachine(StateMachine m);

	public abstract void registerInput(TnccsInputChannel channel);

	public abstract void registerOutput(BatchSender sender);

	public abstract void start(boolean selfInitiated);
	
	public abstract void close();

}