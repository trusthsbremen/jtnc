package org.ietf.nea.pb.handler.pa.dispatch;

import java.util.HashMap;
import java.util.Map;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

public class PaMessageDispatchVendor implements PaMessageDispatchComponent {

	Map<Long,PaMessageDispatchComponent> typeDispatcher = new HashMap<>();
	
	public void add(long id, PaMessageDispatchComponent typeDispatcher){
		this.typeDispatcher.put(id, typeDispatcher);
	}
	
	@Override
	public void remove(long id) {
		if(typeDispatcher.containsKey(id)){
			typeDispatcher.remove(id);
		}
	}
	
	@Override
	public void dispatch(PbMessageValueIm paMessage) {
		if(paMessage != null){
			if(typeDispatcher.containsKey(paMessage.getSubVendorId())){
				typeDispatcher.get(paMessage.getSubType()).dispatch(paMessage);
			}
		}
	}

	@Override
	public void subscribe(IMCConnection connection, Long vendorId,
			Long messageType) {
		if(connection != null){
			if(typeDispatcher.containsKey(messageType)){
				typeDispatcher.get(messageType).subscribe(connection, vendorId, messageType);
			}
		}
	}

	@Override
	public Map<Long, PaMessageDispatchComponent> getChildren() {
		return typeDispatcher;
	}
}
