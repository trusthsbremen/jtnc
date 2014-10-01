package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.PbMessageValueImBuilder;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageImSerializer implements TnccsSerializer<PbMessageValueIm> {

	//private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageTlvFixedLength.IM_VALUE.length();
	
	private PbMessageValueImBuilder builder;
	
	PbMessageImSerializer(PbMessageValueImBuilder builder){
	    this.builder = builder;
	}
	
	
	@Override
	public void encode(final PbMessageValueIm data, final OutputStream out)
			throws SerializationException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* flags 8 bit(s) */
		Set<PbMessageImFlagsEnum> flags = data.getImFlags();
		byte bFlags = 0;
		for (PbMessageImFlagsEnum pbMessageImFlagsEnum : flags) {
			bFlags |= pbMessageImFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(data.getSubVendorId())
								.array(), 5, 8);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Vendor ID could not be written to the buffer.", e, false, 0,
					Long.toString(data.getSubVendorId()));
		}

		/* message type 32 bit(s) */
		byte[] type = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(data.getSubType()).array(), 4,
				8);
		try {
			buffer.write(type);
		} catch (IOException e) {
			throw new SerializationException(
					"Message type could not be written to the buffer.", e, false, 0,
					Long.toString(data.getSubType()));
		}

		/* collector ID */
		byte[] collector = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(data.getCollectorId()).array(),
				6, 8);
		try {
			buffer.write(collector);
		} catch (IOException e) {
			throw new SerializationException(
					"Message collector ID could not be written to the buffer.", e, false, 0,
					Long.toString(data.getCollectorId()));
		}
		
		/* validator ID */
		byte[] validator = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(data.getValidatorId()).array(),
				6, 8);
		try {
			buffer.write(validator);
		} catch (IOException e) {
			throw new SerializationException(
					"Message validator ID could not be written to the buffer.", e, false, 0,
					Long.toString(data.getValidatorId()));
		}

		try {
			buffer.writeTo(out);
			out.write((data.getMessage() != null)? data.getMessage() : new byte[0]); 
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, false, 0);
		}
	}

	@Override
	public PbMessageValueIm decode(final InputStream in, final long length) throws SerializationException, ValidationException {
		PbMessageValueIm value = null; 	
		this.builder.clear();
		
		if(length <= 0){
			return value;
		}
		
		long messageLength = length;

		byte[] buffer = new byte[0];
		try{
			/* flags */
			buffer = ByteArrayHelper.arrayFromStream(in, 1);
			byte imFlags = buffer[0];
			this.builder.setImFlags(imFlags);
		
			/* sub vendor ID */ 
			buffer = ByteArrayHelper.arrayFromStream(in, 3);
			long subVendorId = ByteArrayHelper.toLong(buffer);
			this.builder.setSubVendorId(subVendorId);
			
			/* sub message type */
			buffer = ByteArrayHelper.arrayFromStream(in, 4);
			long subType = ByteArrayHelper.toLong(buffer);
			this.builder.setSubType(subType);
			
			/* collector ID */
			buffer = ByteArrayHelper.arrayFromStream(in, 2);
			long collectorId = ByteArrayHelper.toLong(buffer);
			this.builder.setCollectorId(collectorId);
			
			/* validator ID */
			buffer = ByteArrayHelper.arrayFromStream(in, 2);
			long validatorId = ByteArrayHelper.toLong(buffer);
			this.builder.setValidatorId(validatorId);	
			
		}catch (IOException e){
			throw new SerializationException(
					"Returned data for message value is to short or stream may be closed.", e, true, 0, Integer.toString(buffer.length));
		}
		
		/* PA message */
		byte[] imMessage = new byte[0];
		buffer = new byte[0];
		for(long l = messageLength - 12; l > 0; l -= buffer.length){
			
			try{
				buffer = ByteArrayHelper.arrayFromStream(in, ((l < 65535) ?(int)l : 65535));
			}catch(IOException e){
				throw new SerializationException(
						"Returned data for message value is to short or stream may be closed.", e, true, 0, Integer.toString(buffer.length));
			}
			imMessage = ByteArrayHelper.mergeArrays(imMessage, Arrays.copyOfRange(buffer, 0, buffer.length));
		}
		this.builder.setMessage(imMessage);
		
		value = (PbMessageValueIm)this.builder.toValue();
			
		return value;
	}
}
