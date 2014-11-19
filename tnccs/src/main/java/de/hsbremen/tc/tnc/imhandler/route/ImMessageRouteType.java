package de.hsbremen.tc.tnc.imhandler.route;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hsbremen.tc.tnc.imhandler.module.TnccsImModuleHolder;

class ImMessageRouteType<T> implements ImMessageRouteComponent<TnccsImModuleHolder<T>>{
	
	// Work around for the fact that a Set has no get(Object o) method.
	private final Map<TnccsImModuleHolder<T>,TnccsImModuleHolder<T>> subscribers;
	
	ImMessageRouteType(){
		this.subscribers = new LinkedHashMap<>();
	}
	
	@Override
	public List<TnccsImModuleHolder<T>> findRecipients(Long vendorId, Long messageType) {
		return new LinkedList<>(subscribers.values());
	}

	@Override
	public void subscribe(TnccsImModuleHolder<T> connection, Long vendorId,
			Long messageType) {
		if(connection != null){
			this.subscribers.put(connection,connection);
		}
		
	}
	
	@Override
	public void unSubscribe(TnccsImModuleHolder<T> connection, Long vendorId,
			Long messageType) {
		if(connection != null && this.subscribers.containsKey(connection)){
			this.subscribers.remove(connection);
		}
	}

	@Override
	public long countChildren() {
		return this.subscribers.size();
	}

}
