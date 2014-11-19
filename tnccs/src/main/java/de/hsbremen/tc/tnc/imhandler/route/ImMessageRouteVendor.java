package de.hsbremen.tc.tnc.imhandler.route;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;

public class ImMessageRouteVendor<T> implements ImMessageRouteComponent<TnccsImModuleHolder<T>> {

	private final Map<Long,ImMessageRouteComponent<TnccsImModuleHolder<T>>> typeDispatcher;
	
	ImMessageRouteVendor(){
		this.typeDispatcher = new LinkedHashMap<>();
		// create a always a slot for the ANY subscribers
		this.typeDispatcher.put(TNCConstants.TNC_SUBTYPE_ANY, new ImMessageRouteType<T>());
	}
	
	public void add(long id, ImMessageRouteComponent<TnccsImModuleHolder<T>> typeDispatcher){
		this.typeDispatcher.put(id, typeDispatcher);
	}
	
	@Override
	public List<TnccsImModuleHolder<T>> findRecipients(Long vendorId, Long messageType) {
			
		List<TnccsImModuleHolder<T>> t = new ArrayList<>();
		
		if(this.typeDispatcher.containsKey(vendorId)){
			 t.addAll(this.typeDispatcher.get(vendorId).findRecipients(vendorId, messageType));
		}
		// Dispatch always to the ANY subscribers.
		t.addAll(this.typeDispatcher.get(TNCConstants.TNC_VENDORID_ANY).findRecipients(vendorId, messageType));
		
		return t;
	}

	@Override
	public void subscribe(TnccsImModuleHolder<T> connection, Long vendorId,
			Long messageType) {
		if(connection != null){
			if(!this.typeDispatcher.containsKey(messageType)){
				this.add(messageType, new ImMessageRouteType<T>());
			}
			this.typeDispatcher.get(messageType).subscribe(connection, vendorId, messageType);
		}
	}

	@Override
	public void unSubscribe(TnccsImModuleHolder<T> connection, Long vendorId,
			Long messageType) {
		if(connection != null){
			if(this.typeDispatcher.containsKey(messageType)){
				this.typeDispatcher.get(messageType).unSubscribe(connection, vendorId, messageType);
				if(this.typeDispatcher.get(messageType).countChildren() <= 0){
					this.typeDispatcher.remove(typeDispatcher);
				}
			}
		}
	}
	
	@Override
	public long countChildren() {
		return this.typeDispatcher.size();
	}
}
