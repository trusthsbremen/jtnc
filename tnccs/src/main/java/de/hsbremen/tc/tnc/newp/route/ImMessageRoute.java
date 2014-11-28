package de.hsbremen.tc.tnc.newp.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class ImMessageRoute<T> implements ImMessageRouteComponent<T> {

	private final Map<Long,ImMessageRouteComponent<T>> vendorDispatcher;
	
	public ImMessageRoute(){
		this.vendorDispatcher = new LinkedHashMap<>();
		// create a always a slot for the ANY subscribers
		this.add(TNCConstants.TNC_VENDORID_ANY, new ImMessageRouteVendor<T>());
	}
	
	private void add(long id, ImMessageRouteVendor<T> vendorDispatcher){
		this.vendorDispatcher.put(id, vendorDispatcher);
	}
	
	@Override
	public List<T> findRecipients(Long vendorId, Long messageType) {
			
		List<T> t = new ArrayList<>();
		
		if(this.vendorDispatcher.containsKey(vendorId)){
			 t.addAll(this.vendorDispatcher.get(vendorId).findRecipients(vendorId, messageType));
		}
		// Dispatch always to the ANY subscribers.
		t.addAll(this.vendorDispatcher.get(TNCConstants.TNC_VENDORID_ANY).findRecipients(vendorId, messageType));
		
		return t;
	}

	@Override
	public void subscribe(T connection, SupportedMessageType type) {
		if(connection != null){
			if(!this.vendorDispatcher.containsKey(type.getVendorId())){
				this.add(type.getVendorId(), new ImMessageRouteVendor<T>());
			}
			this.vendorDispatcher.get(type.getVendorId()).subscribe(connection, type);
		}
	}
	
	@Override
	public void unsubscribe(T connection){
		if(connection != null){
			for (Iterator<Long> iter = this.vendorDispatcher.keySet().iterator(); iter.hasNext();) {
				Long vendorId = iter.next();
				this.vendorDispatcher.get(vendorId).unsubscribe(connection);
				if(this.vendorDispatcher.get(vendorId).countChildren() <= 0 && vendorId != TNCConstants.TNC_VENDORID_ANY){
					iter.remove();
				}
			}
		}
	}
	

	@Override
	public long countChildren() {
		return this.vendorDispatcher.size();
	}
}
