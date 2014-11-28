package de.hsbremen.tc.tnc.newp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import de.hsbremen.tc.tnc.im.route.ImMessageRoute;
import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class DefaultImMessageRouter implements ImMessageRouter{

	private final ImMessageRoute<Long> routeMap;
	private final Map<Long,Set<Long>> imIds;
	/* This is the concurrency gateway for the routing */
	private final ReentrantReadWriteLock lock;
	private final Lock readLock;
	private final Lock writeLock;
	
	public DefaultImMessageRouter(){
		this.routeMap = new ImMessageRoute<>();
		this.imIds = new HashMap<>();
		this.lock = new ReentrantReadWriteLock(Boolean.TRUE);
		this.readLock = this.lock.readLock();
		this.writeLock = this.lock.writeLock();
	}
	
	@Override
	public void updateMap(Long primaryId, Set<SupportedMessageType> types) {
		this.writeLock.lock();
		try{
			this.routeMap.unSubscribe(primaryId);
			for (SupportedMessageType type : types) {
				this.routeMap.subscribe(primaryId, type);
			}
		}finally{
			this.writeLock.unlock();
		}
	}

	@Override
	public void remove(Long primaryId) {
		this.writeLock.lock();
		try{
			this.routeMap.unSubscribe(primaryId);
			this.imIds.remove(primaryId);
		}finally{
			this.writeLock.unlock();
		}
		
	}

	@Override
	public void addExclusiveId(Long primaryId, long additionalId) {
		this.writeLock.lock();
		try{
			if(this.imIds.containsKey(primaryId)){
				this.imIds.get(primaryId).add(additionalId);
			}else{
				Set<Long> additional = new HashSet<Long>();
				additional.add(additionalId);
				this.imIds.put(primaryId,additional);
			}
		}finally{
			this.writeLock.unlock();
		}
		
	}

	@Override
	public Set<Long> findRecipientIds(long vendorId,
			long messageType) {
		
		List<Long> result = null;
		this.readLock.lock();
		try{
			result = this.routeMap.findRecipients(vendorId, messageType);
		}finally{
			this.readLock.unlock();
		}
		return (result != null) ? new HashSet<Long>(result): new HashSet<Long>();
	}
	
	@Override
	public Long findExclRecipientId(long address, long vendorId,
			long messageType) {
		
		Long result = null;
		this.readLock.lock();
		try{
			List<Long> recipients = this.routeMap.findRecipients(vendorId, messageType);
			if(recipients.contains(address)){
				result = address;
			}else{
				for (Long long1 : recipients) {
					if(this.imIds.containsKey(long1)){
						if(this.imIds.get(long1).contains(address)){
							result = long1;
						}
					}
				}
			}
		}finally{
			this.readLock.unlock();
		}
		
		return result;
	}

}
