package de.hsbremen.tc.tnc.tnccs.handler;

import org.trustedcomputinggroup.tnc.TNCException;

import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public interface TnccsMessageHandler {

	abstract long getVendorId();
	
	abstract boolean canHandle(TnccsMessage m);

	abstract void handle(TnccsMessage m) throws TNCException;
}
