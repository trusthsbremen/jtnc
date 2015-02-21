package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;
import de.hsbremen.tc.tnc.util.NotNull;

class PaAttributeHeaderWriter implements ImWriter<PaAttributeHeader>{

	@Override
	public void write(final PaAttributeHeader data, final OutputStream out)
			throws SerializationException {
		NotNull.check("Attribute header cannot be null.", data);

		PaAttributeHeader aHead = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* flags 8 bit(s) */
		Set<PaAttributeFlagsEnum> flags = aHead.getFlags();
		byte bFlags = 0;
		for (PaAttributeFlagsEnum paAttributeFlagsEnum : flags) {
			bFlags |= paAttributeFlagsEnum.bit();
		}
		buffer.write(bFlags);

		/* vendor ID 24 bit(s) */
		byte[] vendorId = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(aHead.getVendorId())
								.array(), 5, 8);

		try {
			buffer.write(vendorId);
		} catch (IOException e) {
			throw new SerializationException(
					"Vendor ID could not be written to the buffer.", e, false,
					Long.toString(aHead.getVendorId()));
		}

		/* attribute Type 32 bit(s) */
		byte[] messageType = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(aHead.getAttributeType()).array(), 4,
				8);
		try {
			buffer.write(messageType);
		} catch (IOException e) {
			throw new SerializationException(
					"Attribute type could not be written to the buffer.", e, false,
					Long.toString(aHead.getAttributeType()));
		}

		/* message length 32 bit(s) */
		byte[] length = Arrays.copyOfRange(
				ByteBuffer.allocate(8).putLong(aHead.getLength()).array(),
				4, 8);
		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException(
					"Attribute length could not be written to the buffer.", e, false,
					Long.toString(aHead.getLength()));
		}

		// write to output
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException(
					"Attribute header could not be written to the OutputStream.", e, true);
		}
		
	}

}
