package org.ietf.nea.pt.message;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pt.message.enums.PtTlsMessageTlvFixedLengthEnum;
import org.ietf.nea.pt.message.enums.PtTlsMessageTypeEnum;
import org.ietf.nea.pt.value.AbstractPtTlsMessageValue;
import org.ietf.nea.pt.value.PtTlsMessageValueBuilderIetf;
import org.ietf.nea.pt.value.enums.PtTlsSaslResultEnum;
import org.ietf.nea.pt.value.util.SaslMechanism;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.message.util.DefaultByteBuffer;

public class PtTlsMessageFactoryIetf {

	private static final long VENDORID = IETFConstants.IETF_PEN_VENDORID;

	public static PtTlsMessage createVersionRequest(final long identifier,final short minVersion, final short maxVersion, final short preferredVersion) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_REQUEST.messageType();
		
		return createMessage(identifier,type, PtTlsMessageValueBuilderIetf.createVersionRequestValue(minVersion,maxVersion,preferredVersion));
		
	}
	
	public static PtTlsMessage createVersionResponse(final long identifier,final short selectedVersion) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_VERSION_RESPONSE.messageType();
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createVersionResponseValue(selectedVersion));
	}
	
	public static PtTlsMessage createSaslMechanisms(final long identifier,final SaslMechanism... mechanisms) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISMS.messageType();
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createSaslMechanismsValue(mechanisms));
	}
	
	public static PtTlsMessage createSaslMechanisSelection(final long identifier,final SaslMechanism mechanism) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION.messageType();
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createSaslMechanisSelectionValue(mechanism));
	}

	public static PtTlsMessage createSaslMechanisSelection(final long identifier,final SaslMechanism mechanism, byte[] initialData) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_MECHANISM_SELECTION.messageType();
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createSaslMechanisSelectionValue(mechanism,initialData));

	}
	
	public static PtTlsMessage createSaslAuthenticationData(final long identifier,final byte[] authenticationData) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_AUTHENTICATION_DATA.messageType();
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createSaslAuthenticationDataValue(authenticationData));
		
	}
	
	public static PtTlsMessage createSaslResult(final long identifier,final PtTlsSaslResultEnum result) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.messageType();
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createSaslResultValue(result));
		
	}
	
	public static PtTlsMessage createSaslResult(final long identifier, final PtTlsSaslResultEnum result, byte[] resultData) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_SASL_RESULT.messageType();
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createSaslResultValue(result, resultData));
		
	}
	
	public static PtTlsMessage createError(final long identifier,final long errorVendorId, final long errorCode, final PtTlsMessageHeader messageHeader) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.messageType();
		
		ByteBuffer buffer =  new DefaultByteBuffer(PtTlsMessageTlvFixedLengthEnum.MESSAGE.length());
		
		/* reserved 8 bit(s) */
		buffer.writeByte((byte)0);
		/* vendor ID 24 bit(s) */
		buffer.writeDigits(messageHeader.getVendorId(), (byte)3);
		/* message Type 32 bit(s) */
		buffer.writeUnsignedInt(messageHeader.getMessageType());
		/* message length 32 bit(s) */
		buffer.writeUnsignedInt(messageHeader.getLength());
		/* message identifier 32 bit(s) */
		buffer.writeUnsignedInt(messageHeader.getIdentifier());
		
		byte[] messageCopy = buffer.read(PtTlsMessageTlvFixedLengthEnum.MESSAGE.length());
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createErrorValue(errorVendorId, errorCode, messageCopy));
		
	}
	
	public static PtTlsMessage createError(final long identifier,final long errorVendorId, final long errorCode, final byte[] messageCopy ) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_ERROR.messageType();
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createErrorValue(errorVendorId, errorCode, messageCopy));
		
	}
	
	public static PtTlsMessage createPbBatch(final long identifier,final ByteBuffer tnccsData) throws ValidationException{
		long type = PtTlsMessageTypeEnum.IETF_PT_TLS_PB_BATCH.messageType();
		
		return createMessage(identifier, type, PtTlsMessageValueBuilderIetf.createPbBatchValue(tnccsData));
		
	}
	
	private static PtTlsMessage createMessage(final long identifier,final long type, final AbstractPtTlsMessageValue value) throws ValidationException {
		if(value == null){
			throw new NullPointerException("Value cannot be null.");
		}
		
		if(identifier > 0xFFFFFFFFL){
			throw new IllegalArgumentException("Identifier to large. Identifier must be in range from 0 to " + 0xFFFFFFFFL );
		}
		
	    PtTlsMessageHeaderBuilderIetf mBuilder = new PtTlsMessageHeaderBuilderIetf();
		try{
			
			mBuilder.setVendorId(VENDORID);
			mBuilder.setType(type);
			mBuilder.setLength(PtTlsMessageTlvFixedLengthEnum.MESSAGE.length() + value.getLength());
			mBuilder.setIdentifier(identifier);
			
		}catch(RuleException e){
			throw new ValidationException(e.getMessage(), e, ValidationException.OFFSET_NOT_SET);
		}

		PtTlsMessage message = new PtTlsMessage(mBuilder.toObject(), value);
		
		return message;

	}
}
