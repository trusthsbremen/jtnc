package de.hsbremen.tc.tnc.imhandler.adapter.connection;

import org.ietf.nea.pb.message.PbMessageValueBuilderIetf;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.imhandler.handler.ImConnectionMessageQueue;

class ImcConnectionAdapterIetf implements IMCConnection{

	private ImConnectionMessageQueue messageObserver; 
	
	ImcConnectionAdapterIetf(final ImConnectionMessageQueue messageObserver){
		this.messageObserver = messageObserver;
	}
	
	// STD IMCCon 
	@Override
	public void sendMessage(long messageType, byte[] message)
			throws TNCException {
		long subType = (byte)(messageType & 0xFF);
		long subVendorId = (messageType & 0xFFFFFF00) >>> 8;
		
		this.messageObserver.addMessage(PbMessageValueBuilderIetf.createImValue(null, subVendorId, subType, (short)TNCConstants.TNC_IMCID_ANY, (short)TNCConstants.TNC_IMVID_ANY, message));
	}

	@Override
	public void requestHandshakeRetry(long reason) throws TNCException {
		// TODO where to redirect the handshake retry
		
	}
}
