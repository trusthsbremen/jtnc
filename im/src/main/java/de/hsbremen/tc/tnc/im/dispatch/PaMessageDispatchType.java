package de.hsbremen.tc.tnc.im.dispatch;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;

import de.hsbremen.tc.tnc.command.FunctionCall;
import de.hsbremen.tc.tnc.im.command.PaReceiveFunctionBuilder;
import de.hsbremen.tc.tnc.im.connection.ImConnection;

class PaMessageDispatchType implements PaMessageDispatchComponent{
	
	// Work around for the fact that a Set has no get(Object o) method.
	private final Map<ImConnection, ImConnection> subscribers;
	private final PaReceiveFunctionBuilder receiveFunctionBuilder; 
	
	PaMessageDispatchType(PaReceiveFunctionBuilder builder ){
		this.subscribers = new LinkedHashMap<>();
		this.receiveFunctionBuilder = builder;
	}
	
	@Override
	public void dispatch(PbMessageValueIm paMessage) {
		if(!paMessage.getImFlags().contains(PbMessageImFlagsEnum.EXCL)){
			for (ImConnection connection : this.subscribers.values()) {
				FunctionCall call = receiveFunctionBuilder.build(paMessage);
				
			}
		}else{
			
			Long destination = ( /* TODO constraint */ ) ? paMessage.getValidatorId() : paMessage.getCollectorId(); 
			//TODO get IMCConnection by ID in map; 
		}
		
	}

	@Override
	public void subscribe(ImConnection connection, Long vendorId,
			Long messageType) {
		if(connection != null){
			this.subscribers.put(connection, connection);
		}
		
	}
	
	@Override
	public void unSubscribe(ImConnection connection, Long vendorId,
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
