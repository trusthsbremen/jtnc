package de.hsbremen.tc.tnc.adapter.connection;

import org.ietf.nea.pb.message.PbMessageFactoryIetf;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.trustedcomputinggroup.tnc.ifimc.IMCConnectionLong;
import org.trustedcomputinggroup.tnc.ifimc.TNCException;

import de.hsbremen.tc.tnc.exception.TncException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.exception.enums.TncExceptionCodeEnum;
import de.hsbremen.tc.tnc.session.context.SessionConnectionContext;
import de.hsbremen.tc.tnc.tnccs.message.TnccsMessage;

public class ImcConnectionAdapterIetfLong extends ImcConnectionAdapterIetf implements IMCConnectionLong {
	
	public ImcConnectionAdapterIetfLong(int primaryImcId,
			SessionConnectionContext session) {
		super(primaryImcId, session);
	}

	@Override
	public void sendMessageLong(long messageFlags, long messageVendorID,
			long messageSubtype, byte[] message, long sourceIMCID,
			long destinationIMVID) throws TNCException {

		if(!super.isReceiving()){
			throw new TNCException("Connection is currently not allowed to receive messages.", TncExceptionCodeEnum.TNC_RESULT_ILLEGAL_OPERATION.result());
		}
		
		TnccsMessage m = null;
		try {
			m = PbMessageFactoryIetf.createIm(new PbMessageImFlagsEnum[0], messageVendorID, messageSubtype, (int)sourceIMCID, (int)destinationIMVID, message);
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
