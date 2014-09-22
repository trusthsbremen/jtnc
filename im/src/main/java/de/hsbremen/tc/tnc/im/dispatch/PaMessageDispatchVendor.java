package de.hsbremen.tc.tnc.im.dispatch;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;

import de.hsbremen.tc.tnc.im.command.PaReceiveFunctionBuilder;
import de.hsbremen.tc.tnc.im.connection.ImConnection;

public class PaMessageDispatchVendor implements PaMessageDispatchComponent {

	private final Map<Long,PaMessageDispatchComponent> typeDispatcher;
	private final PaReceiveFunctionBuilder receiveFunctionBuilder; 
	
	PaMessageDispatchVendor(PaReceiveFunctionBuilder builder){
		this.typeDispatcher = new LinkedHashMap<>();
		this.receiveFunctionBuilder = builder;
		// create a always a slot for the ANY subscribers
		this.typeDispatcher.put(TNCConstants.TNC_SUBTYPE_ANY, new PaMessageDispatchType(this.receiveFunctionBuilder));
	}
	
	public void add(long id, PaMessageDispatchComponent typeDispatcher){
		this.typeDispatcher.put(id, typeDispatcher);
	}
	
	@Override
	public void dispatch(PbMessageValueIm paMessage) {
		if(paMessage != null){
			if(this.typeDispatcher.containsKey(paMessage.getSubVendorId())){
				this.typeDispatcher.get(paMessage.getSubType()).dispatch(paMessage);
			}
			// Dispatch always to the ANY subscribers.
			this.typeDispatcher.get(TNCConstants.TNC_SUBTYPE_ANY).dispatch(paMessage);
		}
	}

	@Override
	public void subscribe(ImConnection connection, Long vendorId,
			Long messageType) {
		if(connection != null){
			if(!this.typeDispatcher.containsKey(messageType)){
				this.add(messageType, new PaMessageDispatchType(this.receiveFunctionBuilder));
			}
			this.typeDispatcher.get(messageType).subscribe(connection, vendorId, messageType);
		}
	}

	@Override
	public void unSubscribe(ImConnection connection, Long vendorId,
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
