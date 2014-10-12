package de.hsbremen.tc.tnc.im.route;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.im.module.ImModule;

public class ImMessageRouteVendor<T> implements ImMessageRouteComponent<ImModule<T>> {

	private final Map<Long,ImMessageRouteComponent<ImModule<T>>> typeDispatcher;
	
	ImMessageRouteVendor(){
		this.typeDispatcher = new LinkedHashMap<>();
		// create a always a slot for the ANY subscribers
		this.typeDispatcher.put(TNCConstants.TNC_SUBTYPE_ANY, new ImMessageRouteType<T>());
	}
	
	public void add(long id, ImMessageRouteComponent<ImModule<T>> typeDispatcher){
		this.typeDispatcher.put(id, typeDispatcher);
	}
	
	@Override
	public List<ImModule<T>> findRecipients(Long vendorId, Long messageType) {
			
		List<ImModule<T>> t = new ArrayList<>();
		
		if(this.typeDispatcher.containsKey(vendorId)){
			 t.addAll(this.typeDispatcher.get(vendorId).findRecipients(vendorId, messageType));
		}
		// Dispatch always to the ANY subscribers.
		t.addAll(this.typeDispatcher.get(TNCConstants.TNC_VENDORID_ANY).findRecipients(vendorId, messageType));
		
		return t;
	}

	@Override
	public void subscribe(ImModule<T> connection, Long vendorId,
			Long messageType) {
		if(connection != null){
			if(!this.typeDispatcher.containsKey(messageType)){
				this.add(messageType, new ImMessageRouteType<T>());
			}
			this.typeDispatcher.get(messageType).subscribe(connection, vendorId, messageType);
		}
	}

	@Override
	public void unSubscribe(ImModule<T> connection, Long vendorId,
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
