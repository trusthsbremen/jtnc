package de.hsbremen.tc.tnc.command;

import org.trustedcomputinggroup.tnc.ifimc.TNCException;

public interface FunctionCall {

	void call() throws TNCException;
	
}
