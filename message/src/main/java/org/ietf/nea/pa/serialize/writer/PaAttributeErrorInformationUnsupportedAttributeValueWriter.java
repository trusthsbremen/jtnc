package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Set;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.enums.PaAttributeFlagsEnum;
import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedAttribute;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributeErrorInformationUnsupportedAttributeValueWriter implements ImWriter<PaAttributeValueErrorInformationUnsupportedAttribute>{
	
	private PaMessageHeaderWriter writer;
	
	PaAttributeErrorInformationUnsupportedAttributeValueWriter(
			PaMessageHeaderWriter writer) {
		this.writer = writer;
	}

	@Override
	public void write(final PaAttributeValueErrorInformationUnsupportedAttribute data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueErrorInformationUnsupportedAttribute aValue = data;

		/* message header copy 64 bit(s) */
		this.writer.write(aValue.getMessageHeader(), out);
		
		/* attribute header parameter copy 64 bit(s) */
		this.writeAttribute(aValue.getAttributeHeader(), out);

	}
	
	private void writeAttribute(final PaAttributeHeader aHead, final OutputStream out) throws SerializationException{
		
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
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
