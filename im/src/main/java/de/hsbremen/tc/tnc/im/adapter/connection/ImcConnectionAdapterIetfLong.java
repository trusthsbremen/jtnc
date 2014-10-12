package de.hsbremen.tc.tnc.im.adapter.connection;

import org.ietf.nea.pb.message.PbMessageValueBuilderIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnection;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnectionLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.im.handler.ImConnectionMessageObserver;

class ImcConnectionAdapterIetfLong implements IMCConnection, IMCConnectionLong{

	private ImConnectionMessageObserver messageObserver; 
	private IMCConnection stdConnection;
	
	ImcConnectionAdapterIetfLong(final IMCConnection stdConnection, final ImConnectionMessageObserver messageObserver){
		this.messageObserver = messageObserver;
		this.stdConnection = stdConnection;
	}
	
	// STD IMCCon 
	@Override
	public void sendMessage(long messageType, byte[] message)
			throws TNCException {
		
		this.stdConnection.sendMessage(messageType, message);
	}

	@Override
	public void requestHandshakeRetry(long reason) throws TNCException {
		this.stdConnection.requestHandshakeRetry(reason);
	}

	// LONG IMCCon
	@Override
	public void sendMessageLong(long messageFlags, long messageVendorID,
			long messageSubtype, byte[] message, long sourceIMCID,
			long destinationIMVID) throws TNCException {
		
		PbMessageImFlagsEnum[] imFlags = null;
		if ((byte)(messageFlags & 0x80)  == PbMessageImFlagsEnum.EXCL.bit()) {
			imFlags = new PbMessageImFlagsEnum[]{PbMessageImFlagsEnum.EXCL};
		}
		
		this.messageObserver.addReturnValue(PbMessageValueBuilderIetf.createImValue(imFlags, messageVendorID, messageSubtype, (short)sourceIMCID, (short)destinationIMVID, message));
		
	}	

}
