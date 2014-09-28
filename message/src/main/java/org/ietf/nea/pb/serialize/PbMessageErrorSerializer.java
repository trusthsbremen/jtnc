package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueError;
import org.ietf.nea.pb.message.PbMessageValueErrorBuilder;
import org.ietf.nea.pb.message.enums.PbMessageErrorFlagsEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

public class PbMessageErrorSerializer implements TnccsSerializer<PbMessageValueError> {

	private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageValueError.FIXED_LENGTH;
	
	private PbMessageValueErrorBuilder builder;    
	
	public PbMessageErrorSerializer(PbMessageValueErrorBuilder builder){
	    	this.builder = builder;
	}
	
	
	@Override
	public void encode(final PbMessageValueError data, final OutputStream out)
			throws SerializationException {
		

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* Flags 8 bit(s) */
		Set<PbMessageErrorFlagsEnum> flags = data.getErrorFlags();
		byte bFlags = 0;
		for (PbMessageErrorFlagsEnum pbMessageErrorFlagsEnum : flags) {
			bFlags |= pbMessageErrorFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* Vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(data.getErrorVendorId())
								.array(), 5, 7);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Error vendor ID could not be written to the buffer.", e,
					Long.toString(data.getErrorVendorId()));
		}

		/* Error Code */
		byte[] code = ByteBuffer.allocate(2).putShort(data.getErrorCode()).array();
		try {
			buffer.write(code);
		} catch (IOException e) {
			throw new SerializationException(
					"Error code could not be written to the buffer.", e,
					Short.toString(data.getErrorCode()));
		}
		
		/* Reserved */
		byte[] reserved = ByteBuffer.allocate(2).putShort(data.getReserved()).array();
		try {
			buffer.write(reserved);
		} catch (IOException e) {
			throw new SerializationException(
					"Reserved space could not be written to the buffer.", e,
					Short.toString(data.getReserved()));
		}
		
		/* Error Parameters */
		try {
			buffer.write(data.getErrorParameter());
		} catch (IOException e) {
			throw new SerializationException(
					"Error content could not be written to the buffer.", e,
					Arrays.toString(data.getErrorParameter()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e);
		}
	}

	@Override
	public PbMessageValueError decode(final InputStream in, final long length) throws SerializationException, ValidationException {
		
		PbMessageValueError value = null; 	
		this.builder.clear();
		
		if(length <= 0){
			return value;
		}
		
		long messageLength = length;
		
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

			/* Flags */
			byte errFlags = buffer[0];
			this.builder.setErrorFlags(errFlags);
			
			/* Vendor ID */
			long errorVendorId = ByteArrayHelper.toLong(
					new byte[] { buffer[1], buffer[2], buffer[3] });
			this.builder.setErrorVendorId(errorVendorId);
			
			/* Error Code */
			short errorCode = ByteArrayHelper.toShort(new byte[]{buffer[4], buffer[5]});
			this.builder.setErrorCode(errorCode);
			
			/* Ignore Reserved */
			
			/* Content */
			byte[] errorParameter = new byte[0];
			count = 0;
			for(long l = messageLength - PbMessageValueError.FIXED_LENGTH; l > 0; l -= count){
				
				buffer = (l < 65535) ? new byte[(int)l] : new byte[65535];
				try {
					count = in.read(buffer);
				} catch (IOException e) {
					throw new SerializationException(
							"InputStream could not be read.", e);
				}
				errorParameter = ByteArrayHelper.mergeArrays(errorParameter, Arrays.copyOfRange(buffer, 0, count));
			}
			this.builder.setErrorParameter(errorParameter);
			
			value = (PbMessageValueError)this.builder.toValue();
			
			
		} else {
			throw new SerializationException("Returned data length (" + count
					+ ") for message is to short or stream may be closed.",
					Integer.toString(count));
		}
		
		return value;
	}
}
