package de.hsbremen.tc.tnc.im.dispatch;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.im.command.PaReceiveFunctionBuilder;
import de.hsbremen.tc.tnc.im.connection.ImConnection;

class PaMessageDispatcher implements PaMessageDispatchComponent {

	private final Map<Long,PaMessageDispatchComponent> vendorDispatcher;
	private final PaReceiveFunctionBuilder receiveFunctionBuilder; 
	
	PaMessageDispatcher(PaReceiveFunctionBuilder builder){
		this.vendorDispatcher = new LinkedHashMap<>();
		this.receiveFunctionBuilder = builder;
		// create a always a slot for the ANY subscribers
		this.add(TNCConstants.TNC_VENDORID_ANY, new PaMessageDispatchVendor(this.receiveFunctionBuilder));
	}
	
	private void add(long id, PaMessageDispatchVendor vendorDispatcher){
		this.vendorDispatcher.put(id, vendorDispatcher);
	}
	
	@Override
	public void dispatch(PbMessageValueIm paMessage) {
		if(paMessage != null){
			if(this.vendorDispatcher.containsKey(paMessage.getSubVendorId())){
				this.vendorDispatcher.get(paMessage.getSubVendorId()).dispatch(paMessage);
			}
			// Dispatch always to the ANY subscribers.
			this.vendorDispatcher.get(TNCConstants.TNC_VENDORID_ANY).dispatch(paMessage);
		}
	}

	@Override
	public void subscribe(ImcSession, Long vendorId,
			Long messageType) {
		if(connection != null){
			if(!this.vendorDispatcher.containsKey(vendorId)){
				this.add(vendorId, new PaMessageDispatchVendor(this.receiveFunctionBuilder));
			}
			this.vendorDispatcher.get(vendorId).subscribe(connection, vendorId, messageType);
		}
	}
	
	public void unSubscribe(ImConnection connection, Long vendorId,
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
