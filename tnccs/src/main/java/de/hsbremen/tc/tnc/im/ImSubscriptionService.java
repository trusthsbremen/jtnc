package de.hsbremen.tc.tnc.im;

import java.util.Set;

public interface ImSubscriptionService {

	public Set<Long> subscribe(ImSubscriber subscriber);
	public void unsubscribe(ImSubscriber subscriber);
	public Set<Long> update(ImSubscriber subscriber);
	
	
}
