package de.hsbremen.tc.tnc.im.route;

import java.util.List;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

public interface ImMessageRouteComponent<T> {
	
	// May throw TNCException
	public List<T> findRecipients(Long vendorId, Long messageType);
	public void subscribe(T recipient, SupportedMessageType type);
	public void unSubscribe(T recipient);
	public long countChildren();
	
}
