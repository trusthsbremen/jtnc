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
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.IETFConstants;
import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageRemediationParameterSerializer implements TnccsSerializer<PbMessageValueRemediationParameters> {

	private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageTlvFixedLength.REM_PAR_VALUE.length();

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

		/* Reserved 8 bit(s) */
		buffer.write(data.getReserved());

		/* Vendor ID 24 bit(s) */
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

		/* Remediation Type 32 bit(s) */
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
		
		if(data.getRpVendorId() == IETFConstants.IETF_PEN_VENDORID){
	       	if(data.getRpType() == PbMessageRemediationParameterTypeEnum.IETF_STRING.type()){
	       		this.stringParamsSerializer.encode((PbMessageValueRemediationParameterString)data.getParameter(), out);
	       		
	       	}else if(data.getRpType() == PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
	       		this.uriParamsSerializer.encode((PbMessageValueRemediationParameterUri)data.getParameter(), out);
	       	}
	    }else{
	        	throw new IllegalArgumentException("Unsupported remediation message type with vendor ID "+ data.getRpVendorId() +" and message type "+ data.getRpType() +".");
	    }
	}

	@Override
	public PbMessageValueRemediationParameters decode(final InputStream in, final long length) throws SerializationException, ValidationException {
		PbMessageValueRemediationParameters value = null; 	
		this.builder.clear();
		if(length <= 0){
			return value;
		}

		byte[] buffer = new byte[0];
		
		try{
			buffer = ByteArrayHelper.arrayFromStream(in, MESSAGE_VALUE_FIXED_SIZE);
		}catch(IOException e){
			throw new SerializationException(
						"InputStream could not be read.", e, true, 0);
			
		}

		
		if (buffer.length >= MESSAGE_VALUE_FIXED_SIZE){

			/* Ignore Reserved */

			/* Vendor ID */
			long rpVendorId = ByteArrayHelper.toLong(new byte[] { buffer[1], buffer[2], buffer[3] });
			this.builder.setRpVendorId(rpVendorId);
			
			/* Remediation Type */
			long type = ByteArrayHelper.toLong(new byte[] { buffer[4], buffer[5], buffer[6], buffer[7] });
			this.builder.setRpType(type);

//			AbstractPbMessageValueRemediationParametersValue rpParam = null;
			
			// value length = header length - overall message length
			long valueLength = length - MESSAGE_VALUE_FIXED_SIZE;

			if(type == PbMessageRemediationParameterTypeEnum.IETF_URI.type()){
				PbMessageValueRemediationParameterUri paramUri = this.uriParamsSerializer.decode(in, valueLength);
				this.builder.setParameter(paramUri);
				
			}else if(type == PbMessageRemediationParameterTypeEnum.IETF_STRING.type()){
				PbMessageValueRemediationParameterString paramString = this.stringParamsSerializer.decode(in, valueLength);
		       this.builder.setParameter(paramString);
			}else{
				// TODO ignore or read into some byte array as general parameter type
				throw new SerializationException("Remediation type #"+type+" could not be recognized.", false, 0,Long.toString(type));
			}
			
			value = (PbMessageValueRemediationParameters)this.builder.toValue(); 
			
		} else {
			throw new SerializationException("Returned data length (" + buffer.length
					+ ") for message is to short or stream may be closed.", false, 0,
					Integer.toString(buffer.length));
		}
		
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
