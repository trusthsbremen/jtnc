package de.hsbremen.tc.tnc.tnccs.im.route;

import java.util.Set;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

public interface ImMessageRouter {

	public abstract void updateMap(Long primaryId, Set<SupportedMessageType> type);
	public abstract void remove(Long primaryId);
	public abstract void addExclusiveId(Long primaryId, long additionalId);
	public abstract Set<Long> findRecipientIds(long vendorId, long messageType);
	public abstract Long findExclRecipientId(Long address, long vendorId, long messageType);
}
