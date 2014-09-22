package de.hsbremen.tc.tnc.im.dispatch;

import org.ietf.nea.pb.message.PbMessageValueIm;

import de.hsbremen.tc.tnc.im.connection.ImConnection;

public interface PaMessageDispatchComponent {
	
	// May throw TNCException
	public void dispatch(PbMessageValueIm paMessage);
	public void subscribe(ImConnection connection, Long vendorId, Long messageType);
	public void unSubscribe(ImConnection connection, Long vendorId, Long messageType);
	public long countChildren();
	
}
