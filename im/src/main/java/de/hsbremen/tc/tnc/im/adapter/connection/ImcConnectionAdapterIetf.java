package de.hsbremen.tc.tnc.im.adapter.connection;

import org.ietf.nea.pb.message.PbMessageValueBuilderIetf;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.handler.ImConnectionMessageObserver;

class ImcConnectionAdapterIetf implements IMCConnection{

	private ImConnectionMessageObserver messageObserver; 
	
	ImcConnectionAdapterIetf(final ImConnectionMessageObserver messageObserver){
		this.messageObserver = messageObserver;
	}
	
	// STD IMCCon 
	@Override
	public void sendMessage(long messageType, byte[] message)
			throws TNCException {
		long subType = (byte)(messageType & 0xFF);
		long subVendorId = (messageType & 0xFFFFFF00) >>> 8;
		
		this.messageObserver.addReturnValue(PbMessageValueBuilderIetf.createImValue(null, subVendorId, subType, (short)TNCConstants.TNC_IMCID_ANY, (short)TNCConstants.TNC_IMVID_ANY, message));
	}

	@Override
	public void requestHandshakeRetry(long reason) throws TNCException {
		// TODO where to redirect the handshake retry
		
	}
}
