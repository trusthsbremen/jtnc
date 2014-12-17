package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttribute;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;

class PaAttributeErrorInformationUnsupportedAttributeValueWriter implements ImWriter<PaAttributeValueErrorInformationUnsupportedAttribute>{
	

	@Override
	public void write(final PaAttributeValueErrorInformationUnsupportedAttribute data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueErrorInformationUnsupportedAttribute aValue = data;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		/* message header copy 64 bit(s) */
		/* copy version 8 bit(s) */
		buffer.write(data.getMessageHeader().getVersion());

		/* copy reserved 24 bit(s) */
		try {
			buffer.write(data.getMessageHeader().getReserved());
		} catch (IOException e) {
			throw new SerializationException("Reserved space could not be written to the buffer.",e,false);
		}

		/* copy identifier 32 bit(s) */
		byte[] identifier = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(data.getMessageHeader().getIdentifier())
								.array(), 4, 8);
		try {
			buffer.write(identifier);
		} catch (IOException e) {
			throw new SerializationException("Idenitfier could not be written to the buffer.",e,false);
		}
		
		/* attribute header copy 64 bit(s) */
		PaAttributeHeader aHead = aValue.getAttributeHeader();
		
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
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
