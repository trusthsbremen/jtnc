package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterUri;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.message.PbMessageValueRemediationParametersBuilder;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageRemediationParameterSerializer implements TnccsSerializer<PbMessageValueRemediationParameters> {

//	private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageTlvFixedLength.REM_PAR_VALUE.length();

	private final PbMessageRemediationParameterStringSerializer stringParamsSerializer;
	
	private final PbMessageRemediationParameterUriSerializer uriParamsSerializer;
 
	private PbMessageValueRemediationParametersBuilder builder;
	
	// TODO make this more flexible to other serializers
    PbMessageRemediationParameterSerializer(PbMessageValueRemediationParametersBuilder builder, PbMessageRemediationParameterStringSerializer stringSerializer, PbMessageRemediationParameterUriSerializer uriSerializer) {
    	// Singleton
    	this.stringParamsSerializer = stringSerializer;
    	this.uriParamsSerializer = uriSerializer;
    	this.builder = builder;
    }	

	@Override
	public void encode(final PbMessageValueRemediationParameters data, final OutputStream out)
			throws SerializationException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* reserved 8 bit(s) */
		buffer.write(data.getReserved());

		/* vendor ID 24 bit(s) */
		byte[] rpVendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(data.getRpVendorId())
								.array(), 5, 7);
		try {
			buffer.write(rpVendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Remediation vendor ID could not be written to the buffer.", e, false, 0,
					Long.toString(data.getRpVendorId()));
		}

		/* remediation type 32 bit(s) */
		byte[] rpType = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(data.getRpType()).array(), 4,
				8);
		try {
			buffer.write(rpType);
		} catch (IOException e) {
			throw new SerializationException(
					"Remediation type could not be written to the buffer.", e, false, 0,
					Long.toString(data.getRpType()));
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true, 0);
		}
		
		/* remediation parameter */
		if(data.getRpVendorId() == IETFConstants.IETF_PEN_VENDORID){
	       	if(data.getRpType() == PbMessageRemediationParameterTypeEnum.IETF_STRING.type()){
	       		this.stringParamsSerializer.encode((PbMessageValueRemediationParameterString)data.getParameter(), out);
	       		
	       	}else if(data.getRpType() == PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
	       		this.uriParamsSerializer.encode((PbMessageValueRemediationParameterUri)data.getParameter(), out);
	       	} else {
				throw new SerializationException(
						"Remediation message type is not supported.",false ,0 ,
						Long.toString(data.getRpVendorId()),
						Long.toString(data.getRpType()));
			}
		} else {
			throw new SerializationException(
					"Remediation vendor ID is not supported.",false ,0 ,
					Long.toString(data.getRpVendorId()),
					Long.toString(data.getRpType()));
		}
	}

	@Override
	public PbMessageValueRemediationParameters decode(final InputStream in, final long length) throws SerializationException, ValidationException {
		PbMessageValueRemediationParameters value = null; 	
		this.builder.clear();
		
		if(length <= 0){
			return value;
		}

		long rpVendorId  = 0L;
		long rpType = 0L;
		
		byte[] buffer = new byte[0];
		try{

			/* ignore reserved */
			ByteArrayHelper.arrayFromStream(in, 1);
			
			/* vendor ID */
			buffer = ByteArrayHelper.arrayFromStream(in, 3);
			rpVendorId = ByteArrayHelper.toLong(buffer);
			this.builder.setRpVendorId(rpVendorId);
			
			/* remediation type */
			buffer = ByteArrayHelper.arrayFromStream(in, 4);
			rpType = ByteArrayHelper.toLong(buffer);
			this.builder.setRpType(rpType);
		
		}catch(IOException e){
			throw new SerializationException(
					"Returned data for message value is to short or stream may be closed.", e, true, 0, Integer.toString(buffer.length));
	
		}
		
		/* remediation parameter */
		// value length = header length - overall message length
		long valueLength = length - 8;

		if(rpType == PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
			PbMessageValueRemediationParameterUri paramUri = this.uriParamsSerializer.decode(in, valueLength);
			this.builder.setParameter(paramUri);
			
		}else if(rpType == PbMessageRemediationParameterTypeEnum.IETF_STRING.type()){
			PbMessageValueRemediationParameterString paramString = this.stringParamsSerializer.decode(in, valueLength);
	       this.builder.setParameter(paramString);
		}else{
			// TODO ignore or read into some byte array as general parameter type
			throw new SerializationException("Remediation type #"+rpType+" could not be recognized.", false, 0,Long.toString(rpType));
		}
		
		value = (PbMessageValueRemediationParameters)this.builder.toValue(); 
		
		return value;
	}


//	private AbstractPbMessageValueRemediationParametersValue decodeTypedPbMessageRemediationParameter(final PbMessageRemediationParameterTypeEnum rpType, final InputStream in) throws SerializationException {
//		if(rpType == PbMessageRemediationParameterTypeEnum.IETF_URI){
//			// length is unknown = -1
//			PbMessageValueRemediationParameterUri paramUri = this.uriParamsSerializer.decode(in, -1);
//			return paramUri;
//		}
//		
//		if(rpType == PbMessageRemediationParameterTypeEnum.IETF_STRING){
//        	// length is unknown = -1;
//			PbMessageValueRemediationParameterString paramString = this.stringParamsSerializer.decode(in, -1);
//        	return paramString;
//		}
//		/* should never happen */
//		throw new SerializationException("Remediation type #"+rpType.toString()+" could not be recognized.", rpType.toString());
//	}

}
