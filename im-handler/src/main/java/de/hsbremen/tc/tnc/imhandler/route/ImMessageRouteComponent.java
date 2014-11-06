package de.hsbremen.tc.tnc.imhandler.route;

import java.util.List;

public interface ImMessageRouteComponent<T> {
	
	// May throw TNCException
	public List<T> findRecipients(Long vendorId, Long messageType);
	public void subscribe(T recipient, Long vendorId, Long messageType);
	public void unSubscribe(T recipient, Long vendorId, Long messageType);
	public long countChildren();
	
}
