package de.hsbremen.tc.tnc.imhandler.route;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;

public class ImMessageRoute<T> implements ImMessageRouteComponent<TnccsImModuleHolder<T>> {

	private final Map<Long,ImMessageRouteComponent<TnccsImModuleHolder<T>>> vendorDispatcher;
	
	public ImMessageRoute(){
		this.vendorDispatcher = new LinkedHashMap<>();
		// create a always a slot for the ANY subscribers
		this.add(TNCConstants.TNC_VENDORID_ANY, new ImMessageRouteVendor<T>());
	}
	
	private void add(long id, ImMessageRouteVendor<T> vendorDispatcher){
		this.vendorDispatcher.put(id, vendorDispatcher);
	}
	
	@Override
	public List<TnccsImModuleHolder<T>> findRecipients(Long vendorId, Long messageType) {
			
		List<TnccsImModuleHolder<T>> t = new ArrayList<>();
		
		if(this.vendorDispatcher.containsKey(vendorId)){
			 t.addAll(this.vendorDispatcher.get(vendorId).findRecipients(vendorId, messageType));
		}
		// Dispatch always to the ANY subscribers.
		t.addAll(this.vendorDispatcher.get(TNCConstants.TNC_VENDORID_ANY).findRecipients(vendorId, messageType));
		
		return t;
	}

	@Override
	public void subscribe(TnccsImModuleHolder<T> connection, Long vendorId,
			Long messageType) {
		if(connection != null){
			if(!this.vendorDispatcher.containsKey(vendorId)){
				this.add(vendorId, new ImMessageRouteVendor<T>());
			}
			this.vendorDispatcher.get(vendorId).subscribe(connection, vendorId, messageType);
		}
	}
	
	public void unSubscribe(TnccsImModuleHolder<T> connection, Long vendorId,
			Long messageType){
		if(connection != null){
			if(this.vendorDispatcher.containsKey(vendorId)){
				this.vendorDispatcher.get(vendorId).unSubscribe(connection, vendorId, messageType);
				if(this.vendorDispatcher.get(vendorId).countChildren() <= 0){
					this.vendorDispatcher.remove(vendorId);
				}
			}
		}
	}
	

	@Override
	public long countChildren() {
		return this.vendorDispatcher.size();
	}
}