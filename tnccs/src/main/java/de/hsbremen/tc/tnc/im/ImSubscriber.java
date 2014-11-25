package de.hsbremen.tc.tnc.im;


public interface ImSubscriber {

	public abstract void notifyTerminate(ImSubscriptionService subscriptionService);

	public abstract void notifyDelete(ImSubscriptionService subscriptionService, Long id);

	public abstract void notifyAdd(ImSubscriptionService subscriptionService, Long id);
}
