package org.ietf.nea.pb.handler.pa.dispatch;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

public class PaMessageDispatchType implements PaMessageDispatchComponent{

	private final boolean isServer;
	
	public PaMessageDispatchType(boolean isServer){
		this.isServer = isServer;
	}
	
	// Work around for the fact that a Set has no get(Object o) method.
	Map<IMCConnection, IMCConnection> subscribers = new LinkedHashMap<>();
	
	@Override
	public void dispatch(PbMessageValueIm paMessage) {
		if(!paMessage.getImFlags().contains(PbMessageImFlagsEnum.EXCL)){
			for (IMCConnection connection : subscribers.values()) {
				//TODO make Task here for Support Instance of Long
				
			}
		}else{
			
			Long destination = (isServer) ? paMessage.getValidatorId() : paMessage.getCollectorId(); 
			//TODO get IMCConnection by ID in map; 
		}
		
	}

	@Override
	public void subscribe(IMCConnection connection, Long vendorId,
			Long messageType) {
		this.subscribers.put(connection, connection);
		
	}

	@Override
	public void add(long id, PaMessageDispatchComponent dispatcher) {
		throw new UnsupportedOperationException("Operation is not supported in this implementation " +PaMessageDispatchType.class.getCanonicalName()+ " of " + PaMessageDispatchComponent.class.getCanonicalName() + ".");
		
	}

	@Override
	public void remove(long id) {
		throw new UnsupportedOperationException("Operation is not supported in this implementation " +PaMessageDispatchType.class.getCanonicalName()+ " of " + PaMessageDispatchComponent.class.getCanonicalName() + ".");

	}

	@Override
	public Map<Long, PaMessageDispatchComponent> getChildren() {
		throw new UnsupportedOperationException("Operation is not supported in this implementation " +PaMessageDispatchType.class.getCanonicalName()+ " of " + PaMessageDispatchComponent.class.getCanonicalName() + ".");

	}

}
