package de.hsbremen.tc.tnc.imhandler.adapter.connection;

import de.hsbremen.tc.tnc.imhandler.handler.ImConnectionMessageQueue;


interface ImConnectionAdapterBuilder<S,T> {
	
	public T buildAdapter(S type, ImConnectionMessageQueue observer);
}
