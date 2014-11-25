package de.hsbremen.tc.tnc.im.route;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

class ImMessageRouteType<T> implements ImMessageRouteComponent<T>{
	
	// Work around for the fact that a Set has no get(Object o) method.
	private final Map<T,T> subscribers;
	
	ImMessageRouteType(){
		this.subscribers = new LinkedHashMap<>();
	}
	
	@Override
	public List<T> findRecipients(Long vendorId, Long messageType) {
		return new LinkedList<>(subscribers.values());
	}

	@Override
	public void subscribe(T connection, SupportedMessageType type) {
		if(connection != null){
			this.subscribers.put(connection,connection);
		}
		
	}
	
	@Override
	public void unSubscribe(T connection) {
		if(connection != null && this.subscribers.containsKey(connection)){
			this.subscribers.remove(connection);
		}
	}

	@Override
	public long countChildren() {
		return this.subscribers.size();
	}

}
