package org.ietf.nea.pb.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageValueIm;
import org.ietf.nea.pb.message.enums.PbMessageImFlagsEnum;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;

class PbMessageImValueWriter implements TnccsWriter<PbMessageValueIm>{

	@Override
	public void write(final PbMessageValueIm data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message value cannot be NULL.");
		}
		
		PbMessageValueIm mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* flags 8 bit(s) */
		Set<PbMessageImFlagsEnum> flags = mValue.getImFlags();
		byte bFlags = 0;
		for (PbMessageImFlagsEnum pbMessageImFlagsEnum : flags) {
			bFlags |= pbMessageImFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(mValue.getSubVendorId())
								.array(), 5, 8);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Vendor ID could not be written to the buffer.", e, false,
					Long.toString(mValue.getSubVendorId()));
		}

		/* message type 32 bit(s) */
		byte[] type = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(mValue.getSubType()).array(), 4,
				8);
		try {
			buffer.write(type);
		} catch (IOException e) {
			throw new SerializationException(
					"Message type could not be written to the buffer.", e, false,
					Long.toString(mValue.getSubType()));
		}

		/* collector ID */
		byte[] collector = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(mValue.getCollectorId()).array(),
				6, 8);
		try {
			buffer.write(collector);
		} catch (IOException e) {
			throw new SerializationException(
					"Message collector ID could not be written to the buffer.", e, false,
					Long.toString(mValue.getCollectorId()));
		}
		
		/* validator ID */
		byte[] validator = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(mValue.getValidatorId()).array(),
				6, 8);
		try {
			buffer.write(validator);
		} catch (IOException e) {
			throw new SerializationException(
					"Message validator ID could not be written to the buffer.", e, false,
					Long.toString(mValue.getValidatorId()));
		}

		try {
			buffer.writeTo(out);
			
			/* im message */
			out.write((mValue.getMessage() != null)? mValue.getMessage() : new byte[0]); 
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
