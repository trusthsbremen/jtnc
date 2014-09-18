package org.ietf.nea.pb.handler.pa.dispatch;

import java.util.Map;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;

public interface PaMessageDispatchComponent {
	
	// May throw TNCException
	public void dispatch(PbMessageValueIm paMessage);
	public void subscribe(IMCConnection connection, Long vendorId, Long messageType);
	public void add(long id, PaMessageDispatchComponent dispatcher);
	public void remove(long id);
	public Map<Long, PaMessageDispatchComponent> getChildren();
}
