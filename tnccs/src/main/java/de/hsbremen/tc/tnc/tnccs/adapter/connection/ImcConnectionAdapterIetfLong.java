package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnectionLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

class ImcConnectionAdapterIetfLong extends ImcConnectionAdapterIetf implements IMCConnectionLong {
	
	ImcConnectionAdapterIetfLong(int primaryImcId,
			ImConnectionContext session) {
		super(primaryImcId, session);
	}

	@Override
	public void sendMessageLong(long messageFlags, long messageVendorID,
			long messageSubtype, byte[] message, long sourceIMCID,
			long destinationIMVID) throws TNCException {

		if(messageVendorID == TNCConstants.TNC_VENDORID_ANY || messageSubtype == TNCConstants.TNC_SUBTYPE_ANY){
			throw new TNCException("Message type is set to reserved type.", TNCException.TNC_RESULT_INVALID_PARAMETER);
		}
		
		if(!super.isReceiving()){
			throw new TNCException("Connection is currently not allowed to receive messages.", TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION.id());
		}
		
		super.checkMessageSize(message.length);
		
		TnccsMessage m = null;
		try {
			m = PbMessageFactoryIetf.createIm(new PbMessageImFlagsEnum[0], messageVendorID, messageSubtype, (int)sourceIMCID, (int)destinationIMVID, message);
		} catch (ValidationException e) {
			throw new TNCException(e.getCause().getMessage(), e.getCause().getErrorCode());
		}
		
		try{
			super.sendMessage(m);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().id());
		}
		
	}

}
