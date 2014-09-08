package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueBuilderIetf;
import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

public class PbMessageImSerializer implements TnccsSerializer<PbMessageValueIm> {

	private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageValueIm.FIXED_LENGTH;
	
	private static final class Singleton{
		private static final PbMessageImSerializer INSTANCE = new  PbMessageImSerializer();  
	}
	
	public static  PbMessageImSerializer getInstance(){
	    	return Singleton.INSTANCE;
	}
	    
	private  PbMessageImSerializer(){
	    	// Singleton
	}
	
	
	@Override
	public void encode(final PbMessageValueIm data, final OutputStream out)
			throws SerializationException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* Flags 8 bit(s) */
		Set<PbMessageImFlagsEnum> flags = data.getImFlags();
		byte bFlags = 0;
		for (PbMessageImFlagsEnum pbMessageImFlagsEnum : flags) {
			bFlags |= pbMessageImFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* Vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(data.getSubVendorId())
								.array(), 5, 8);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Vendor ID could not be written to the buffer.", e,
					Long.toString(data.getSubVendorId()));
		}

		/* Message Type 32 bit(s) */
		byte[] type = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(data.getSubType()).array(), 4,
				8);
		try {
			buffer.write(type);
		} catch (IOException e) {
			throw new SerializationException(
					"Message type could not be written to the buffer.", e,
					Long.toString(data.getSubType()));
		}

		/* Collector ID */
		byte[] collector = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(data.getCollectorId()).array(),
				6, 8);
		try {
			buffer.write(collector);
		} catch (IOException e) {
			throw new SerializationException(
					"Message collector ID could not be written to the buffer.", e,
					Long.toString(data.getCollectorId()));
		}
		
		/* Validator ID */
		byte[] validator = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(data.getValidatorId()).array(),
				6, 8);
		try {
			buffer.write(validator);
		} catch (IOException e) {
			throw new SerializationException(
					"Message validator ID could not be written to the buffer.", e,
					Long.toString(data.getValidatorId()));
		}

		try {
			buffer.writeTo(out);
			out.write((data.getMessage() != null)? data.getMessage() : new byte[0]); 
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e);
		}
	}

	@Override
	public PbMessageValueIm decode(final InputStream in, final long length) throws SerializationException {
		PbMessageValueIm value = null; 	
		
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
			PbMessageImFlagsEnum[] imFlags = null;
			// All reserved bits must be ignored
			PbMessageImFlagsEnum flag = PbMessageImFlagsEnum.fromBit((byte)(buffer[0] & PbMessageImFlagsEnum.EXCL.bit()));
			if(flag != null && flag == PbMessageImFlagsEnum.EXCL){
				imFlags = new PbMessageImFlagsEnum[]{flag};
			}else{
				EnumSet<PbMessageImFlagsEnum>flags = EnumSet.noneOf(PbMessageImFlagsEnum.class);
				imFlags = flags.toArray(new PbMessageImFlagsEnum[flags.size()]);
			}
			
			long subVendorId = ByteArrayHelper.toLong(
					new byte[] { buffer[1], buffer[2], buffer[3] });
			long subType = ByteArrayHelper.toLong(
					new byte[] { buffer[4], buffer[5], buffer[6], buffer[7] });
			short collectorId = ByteArrayHelper.toShort(
					new byte[] { buffer[8], buffer[9]});
			short validatorId = ByteArrayHelper.toShort( 
					new byte[] { buffer[10], buffer[11]});
		
			byte[] imMessage = new byte[0];
			count = 0;
			for(long l = messageLength-PbMessageValueIm.FIXED_LENGTH; l > 0; l -= count){
				
				buffer = (l < 65535) ? new byte[(int)l] : new byte[65535];
				try {
					count = in.read(buffer);
				} catch (IOException e) {
					throw new SerializationException(
							"InputStream could not be read.", e);
				}
				imMessage = ByteArrayHelper.mergeArrays(imMessage, Arrays.copyOfRange(buffer, 0, count));
			}
			
			value = PbMessageValueBuilderIetf.createImValue(imFlags, subVendorId, subType, collectorId, validatorId, imMessage);
			
		} else {
			throw new SerializationException("Returned data length (" + count
					+ ") for message is to short or stream may be closed.",
					Integer.toString(count));
		}
		
		return value;
	}
}
