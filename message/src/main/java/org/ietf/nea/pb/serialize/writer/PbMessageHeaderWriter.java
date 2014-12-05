package org.ietf.nea.pb.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.enums.PbMessageFlagsEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsWriter;

class PbMessageHeaderWriter implements TnccsWriter<PbMessageHeader>{

	@Override
	public void write(final PbMessageHeader data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}

		PbMessageHeader mHead = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* flags 8 bit(s) */
		Set<PbMessageFlagsEnum> flags = mHead.getFlags();
		byte bFlags = 0;
		for (PbMessageFlagsEnum pbMessageFlagsEnum : flags) {
			bFlags |= pbMessageFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(mHead.getVendorId())
								.array(), 5, 8);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Vendor ID could not be written to the buffer.", e, false,
					Long.toString(mHead.getVendorId()));
		}

		/* message Type 32 bit(s) */
		byte[] messageType = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(mHead.getMessageType()).array(), 4,
				8);
		try {
			buffer.write(messageType);
		} catch (IOException e) {
			throw new SerializationException(
					"Message type could not be written to the buffer.", e, false,
					Long.toString(mHead.getMessageType()));
		}

		/* message length 32 bit(s) */
		byte[] length = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(mHead.getLength()).array(),
				4, 8);
		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException(
					"Message length could not be written to the buffer.", e, false,
					Long.toString(mHead.getLength()));
		}

		// write to output
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException(
					"Message header could not be written to the OutputStream.", e, true);
		}
		
	}

}
