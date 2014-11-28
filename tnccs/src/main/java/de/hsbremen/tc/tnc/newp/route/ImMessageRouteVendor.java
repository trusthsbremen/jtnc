package de.hsbremen.tc.tnc.newp.route;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.report.SupportedMessageType;

public class ImMessageRouteVendor<T> implements ImMessageRouteComponent<T> {

	private final Map<Long,ImMessageRouteComponent<T>> typeDispatcher;
	
	ImMessageRouteVendor(){
		this.typeDispatcher = new LinkedHashMap<>();
		// create a always a slot for the ANY subscribers
		this.typeDispatcher.put(TNCConstants.TNC_SUBTYPE_ANY, new ImMessageRouteType<T>());
	}
	
	public void add(long id, ImMessageRouteComponent<T> typeDispatcher){
		this.typeDispatcher.put(id, typeDispatcher);
	}
	
	@Override
	public List<T> findRecipients(Long vendorId, Long messageType) {
			
		List<T> t = new ArrayList<>();
		
		if(this.typeDispatcher.containsKey(messageType)){
			 t.addAll(this.typeDispatcher.get(messageType).findRecipients(vendorId, messageType));
		}
		// Dispatch always to the ANY subscribers.
		t.addAll(this.typeDispatcher.get(TNCConstants.TNC_SUBTYPE_ANY).findRecipients(vendorId, messageType));
		
		return t;
	}

	@Override
	public void subscribe(T connection, SupportedMessageType type) {
		if(connection != null){
			if(!this.typeDispatcher.containsKey(type.getType())){
				this.add(type.getType(), new ImMessageRouteType<T>());
			}
			this.typeDispatcher.get(type.getType()).subscribe(connection, type);
		}
	}

	@Override
	public void unsubscribe(T connection) {
		if(connection != null){
			for (Iterator<Long> iter = this.typeDispatcher.keySet().iterator(); iter.hasNext();) {
				Long type = iter.next();
				this.typeDispatcher.get(type).unsubscribe(connection);
				if(this.typeDispatcher.get(type).countChildren() <= 0 && type != TNCConstants.TNC_SUBTYPE_ANY){
					iter.remove();
				}
			}
		}
	}
	
	@Override
	public long countChildren() {
		return this.typeDispatcher.size();
	}
}
