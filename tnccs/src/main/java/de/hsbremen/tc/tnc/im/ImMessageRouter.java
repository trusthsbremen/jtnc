package de.hsbremen.tc.tnc.im;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.trustedcomputinggroup.tnc.ifimc.IMC;

import de.hsbremen.tc.tnc.newp.route.ImMessageRoute;
import de.hsbremen.tc.tnc.report.SupportedMessageType;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessageValue;

public class ImMessageRouter {

	private final ImMessageRoute<IMC> routeMap;
	/* This is the concurrency gateway for the routing */
	private final ReentrantReadWriteLock lock;
	private final Lock readLock;
	private final Lock writLock;
	
	public ImMessageRouter(){
		this.routeMap = new ImMessageRoute<>();
		this.lock = new ReentrantReadWriteLock(Boolean.TRUE);
		this.readLock = this.lock.readLock();
		this.writLock = this.lock.writeLock();
	}
	
	public void createMap(Map<IMC, Imc> loadedModules) {
		
		this.writLock.lock();
		try{
			Set<Entry<IMC,Imc>> entries = loadedModules.entrySet();
			for (Entry<IMC, Imc> entry : entries) {
				Set<SupportedMessageType> supportedMessageTypes = entry.getValue().getSupportedMessageTypes();
				for (SupportedMessageType supportedMessageType : supportedMessageTypes) {
					routeMap.subscribe(entry.getKey(),supportedMessageType.getVendorId(), supportedMessageType.getType());
				}
			
			}
		}finally{
			this.writLock.unlock();
		}
		
	}

	public void addToMap(IMC imc, Imc added) {
		
		this.writLock.lock();
		try{
			Set<SupportedMessageType> supportedMessageTypes = added.getSupportedMessageTypes();
			for (SupportedMessageType supportedMessageType : supportedMessageTypes) {
				routeMap.subscribe(imc, supportedMessageType.getVendorId(), supportedMessageType.getType());
			}
		}finally{
			this.writLock.unlock();
		}
		
	}
	
	public void removeFromMap(IMC imc, Imc removed) {
		this.writLock.lock();
		try{
			Set<SupportedMessageType> supportedMessageTypes = removed.getSupportedMessageTypes();
			for (SupportedMessageType supportedMessageType : supportedMessageTypes) {
				routeMap.unSubscribe(imc, supportedMessageType.getVendorId(), supportedMessageType.getType());
			}
		}finally{
			this.writLock.unlock();
		}
	}
	
	public List<IMC> findRecipients(Set<IMC> keySet, TnccsMessageValue value) {
		List<IMC> recipients = new LinkedList<>();
		this.readLock.lock();
		try{
			if(value instanceof PbMessageValueIm){
				recipients = this.routeMap.findRecipients(((PbMessageValueIm)value).getSubVendorId(), ((PbMessageValueIm)value).getSubType());
			}
		}finally{
			this.writLock.unlock();
		}
		return recipients;
	}

}
