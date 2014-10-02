package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.PbMessageValueErrorBuilder;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageErrorSerializer implements TnccsSerializer<PbMessageValueError> {

	//private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageTlvFixedLength.ERR_VALUE.length();
	
	private PbMessageValueErrorBuilder builder;    
	
	PbMessageErrorSerializer(PbMessageValueErrorBuilder builder){
	    	this.builder = builder;
	}
	
	
	@Override
	public void encode(final PbMessageValueError data, final OutputStream out)
			throws SerializationException {
		

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* flags 8 bit(s) */
		Set<PbMessageErrorFlagsEnum> flags = data.getErrorFlags();
		byte bFlags = 0;
		for (PbMessageErrorFlagsEnum pbMessageErrorFlagsEnum : flags) {
			bFlags |= pbMessageErrorFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(data.getErrorVendorId())
								.array(), 5, 7);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Error vendor ID could not be written to the buffer.", e, false, 0,
					Long.toString(data.getErrorVendorId()));
		}

		/* error Code */
		byte[] code = ByteBuffer.allocate(2).putShort(data.getErrorCode()).array();
		try {
			buffer.write(code);
		} catch (IOException e) {
			throw new SerializationException(
					"Error code could not be written to the buffer.", e, false, 0,
					Short.toString(data.getErrorCode()));
		}
		
		/* reserved */
		byte[] reserved = ByteBuffer.allocate(2).putShort(data.getReserved()).array();
		try {
			buffer.write(reserved);
		} catch (IOException e) {
			throw new SerializationException(
					"Reserved space could not be written to the buffer.", e, false, 0,
					Short.toString(data.getReserved()));
		}
		
		/* error Parameters */
		try {
			buffer.write(data.getErrorParameter());
		} catch (IOException e) {
			throw new SerializationException(
					"Error content could not be written to the buffer.", e, false, 0,
					Arrays.toString(data.getErrorParameter()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true, 0);
		}
	}

	@Override
	public PbMessageValueError decode(final InputStream in, final long length) throws SerializationException, RuleException {
		
		PbMessageValueError value = null; 	
		this.builder.clear();
		
		if(length <= 0){
			return value;
		}
		
		long messageLength = length;
		
		byte [] buffer = new byte[0];
		try{
			/* flags */
			buffer = ByteArrayHelper.arrayFromStream(in, 1);
			byte errFlags = buffer[0];
			this.builder.setErrorFlags(errFlags);
			
			/* vendor ID */
			buffer = ByteArrayHelper.arrayFromStream(in, 3);
			long errorVendorId = ByteArrayHelper.toLong(buffer);
			this.builder.setErrorVendorId(errorVendorId);
			
			/* error Code */
			buffer = ByteArrayHelper.arrayFromStream(in, 2);
			short errorCode = ByteArrayHelper.toShort(buffer);
			this.builder.setErrorCode(errorCode);
			
			/* ignore Reserved */
			ByteArrayHelper.arrayFromStream(in, 2);
			
		}catch (IOException e){
			throw new SerializationException(
					"Returned data for message value is to short or stream may be closed.", e, true, 0, Integer.toString(buffer.length));
		
		}
		
		/* Content */
		buffer = new byte[0];
		byte[] errorParameter = new byte[0];

		for(long l = messageLength - 8; l > 0; l -= buffer.length){

			try{
				buffer = ByteArrayHelper.arrayFromStream(in, (l < 65535) ?(int)l : 65535);
			}catch(IOException e){
				throw new SerializationException(
						"Returned data for message value is to short or stream may be closed.", e, true, 0, Integer.toString(buffer.length));
			
			}
			errorParameter = ByteArrayHelper.mergeArrays(errorParameter, Arrays.copyOfRange(buffer, 0, buffer.length));
		}
		this.builder.setErrorParameter(errorParameter);
		
		value = (PbMessageValueError)this.builder.toValue();
		
		return value;
	}
}
