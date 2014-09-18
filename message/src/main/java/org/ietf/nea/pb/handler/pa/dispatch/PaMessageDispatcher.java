package org.ietf.nea.pb.handler.pa.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

public class PaMessageDispatcher implements PaMessageDispatchComponent {

	Map<Long,PaMessageDispatchComponent> vendorDispatcher = new HashMap<>();
	
	public void add(long id, PaMessageDispatchComponent vendorDispatcher){
		this.vendorDispatcher.put(id, vendorDispatcher);
	}
	
	@Override
	public void remove(long id) {
		if(vendorDispatcher.containsKey(id)){
			Map<Long, PaMessageDispatchComponent> children = vendorDispatcher.get(id).getChildren();
			for (Long key : children.keySet()) {
				vendorDispatcher.remove(key);
			}
			vendorDispatcher.remove(id);
		}
		
	}
	
	@Override
	public void dispatch(PbMessageValueIm paMessage) {
		if(paMessage != null){
			if(vendorDispatcher.containsKey(paMessage.getSubVendorId())){
				vendorDispatcher.get(paMessage.getSubVendorId()).dispatch(paMessage);
			}
		}
	}

	@Override
	public void subscribe(IMCConnection connection, Long vendorId,
			Long messageType) {
		if(connection != null){
			if(vendorDispatcher.containsKey(vendorId)){
				vendorDispatcher.get(vendorId).subscribe(connection, vendorId, messageType);
			}
		}
	}

	@Override
	public Map<Long, PaMessageDispatchComponent> getChildren() {
		return vendorDispatcher;
	}
}
