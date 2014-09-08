package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.IETFConstants;
import org.ietf.nea.pb.message.PbMessageValueBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterString;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameterUri;
import org.ietf.nea.pb.message.PbMessageValueRemediationParameters;
import org.ietf.nea.pb.message.enums.PbMessageRemediationParameterTypeEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

public class PbMessageRemediationParameterSerializer implements TnccsSerializer<PbMessageValueRemediationParameters> {

	private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageValueRemediationParameters.FIXED_LENGTH;
	
	private static final class Singleton{
		static final PbMessageRemediationParameterSerializer INSTANCE = new  PbMessageRemediationParameterSerializer();  
	}
	
	private final PbMessageRemediationParameterStringSerializer stringParamsSerializer;
	
	private final PbMessageRemediationParameterUriSerializer uriParamsSerializer;

	public static  PbMessageRemediationParameterSerializer getInstance(){
	    	return Singleton.INSTANCE;
	}
 
    private PbMessageRemediationParameterSerializer() {
    	// Singleton
    	this.stringParamsSerializer = PbMessageRemediationParameterStringSerializer.getInstance();
    	this.uriParamsSerializer = PbMessageRemediationParameterUriSerializer.getInstance();
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
					"Remediation vendor ID could not be written to the buffer.", e,
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
					"Remediation type could not be written to the buffer.", e,
					Long.toString(data.getRpType()));
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e);
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
	public PbMessageValueRemediationParameters decode(final InputStream in, final long length) throws SerializationException {
		PbMessageValueRemediationParameters value = null; 	

		// ignore any given length and find out on your own.

		byte[] buffer = new byte[MESSAGE_VALUE_FIXED_SIZE];

		int count = 0;
		// wait till data is available
		while (count == 0) {
			try {
				count = in.read(buffer);
			} catch (IOException e) {
				throw new SerializationException(
						"InputStream could not be read.", e);
			}
		}

		
		if (count >= MESSAGE_VALUE_FIXED_SIZE){

			/* Ignore Reserved */
			byte reserved = 0;
			
			/* Vendor ID */
			long rpVendorId = ByteArrayHelper.toLong(new byte[] { buffer[1], buffer[2], buffer[3] });
			
			/* Remediation Type */
			long type = ByteArrayHelper.toLong(new byte[] { buffer[4], buffer[5], buffer[6], buffer[7] });
			
			PbMessageRemediationParameterTypeEnum rpType = PbMessageRemediationParameterTypeEnum.fromType(type);
		
//			AbstractPbMessageValueRemediationParametersValue rpParam = null;
			
			if(rpType != null){
				if(rpType == PbMessageRemediationParameterTypeEnum.IETF_URI){
					// length is unknown = -1
					PbMessageValueRemediationParameterUri paramUri = this.uriParamsSerializer.decode(in, -1);
					value = PbMessageValueBuilderIetf.createRemediationParameterUri(reserved, rpVendorId, rpType.type(), paramUri.getRemediationUri().toString());
				}else if(rpType == PbMessageRemediationParameterTypeEnum.IETF_STRING){
		        	// length is unknown = -1;
					PbMessageValueRemediationParameterString paramString = this.stringParamsSerializer.decode(in, -1);
		        	value = PbMessageValueBuilderIetf.createRemediationParameterString(reserved, rpVendorId, rpType.type(), paramString.getRemediationString(), paramString.getLangCode());
				}else{
					throw new SerializationException("Remediation type #"+rpType.toString()+" could not be recognized.", rpType.toString());
				}
			}else{
				throw new SerializationException("Remediation type #"+type+" could not be recognized.", Long.toString(type));
			}
		} else {
			throw new SerializationException("Returned data length (" + count
					+ ") for message is to short or stream may be closed.",
					Integer.toString(count));
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
