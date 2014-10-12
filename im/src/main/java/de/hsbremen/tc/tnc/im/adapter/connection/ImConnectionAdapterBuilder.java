package de.hsbremen.tc.tnc.im.adapter.connection;

import de.hsbremen.tc.tnc.im.handler.ImConnectionMessageObserver;


interface ImConnectionAdapterBuilder<S,T> {
	
	public T buildAdapter(S type, ImConnectionMessageObserver observer);
}
