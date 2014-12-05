package de.hsbremen.tc.tnc.tnccs.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.TNCConstants;
import org.trustedcomputinggroup.tnc.ifimv.TNCException;
import org.trustedcomputinggroup.tnc.ifimv.IMVConnectionLong;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.message.TnccsMessage;

public class ImvConnectionAdapterIetfLong extends ImvConnectionAdapterIetf implements IMVConnectionLong {
	
	public ImvConnectionAdapterIetfLong(int primaryImcId,
			ImvConnectionContext session) {
		super(primaryImcId, session);
	}

	@Override
	public void sendMessageLong(long messageFlags, long messageVendorID,
			long messageSubtype, byte[] message, long sourceIMVID,
			long destinationIMCID) throws TNCException {

		if(messageVendorID == TNCConstants.TNC_VENDORID_ANY || messageSubtype == TNCConstants.TNC_SUBTYPE_ANY){
			throw new TNCException("Message type is set to reserved type.", TNCException.TNC_RESULT_INVALID_PARAMETER);
		}
		
		if(!super.isReceiving()){
			throw new TNCException("Connection is currently not allowed to receive messages.", TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION.result());
		}
		
		super.checkMessageSize(message.length);
		
		TnccsMessage m = null;
		try {
			m = PbMessageFactoryIetf.createIm(new PbMessageImFlagsEnum[0], messageVendorID, messageSubtype, (int)destinationIMCID, (int)sourceIMVID, message);
		} catch (ValidationException e) {
			throw new TNCException(e.getCause().getMessage(), e.getCause().getErrorCode());
		}
		
		try{
			super.sendMessage(m);
		} catch (TncException e) {
			throw new TNCException(e.getMessage(), e.getResultCode().result());
		}
		
	}

}
