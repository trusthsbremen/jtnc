package de.hsbremen.tc.tnc.im.adapter.connection;

import de.hsbremen.tc.tnc.im.handler.ImConnectionMessageQueue;


interface ImConnectionAdapterBuilder<S,T> {
	
	public T buildAdapter(S type, ImConnectionMessageQueue observer);
}
