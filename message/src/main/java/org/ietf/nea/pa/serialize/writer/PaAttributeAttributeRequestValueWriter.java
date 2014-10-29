package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.util.AttributeReference;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributeAttributeRequestValueWriter implements ImWriter<PaAttributeValueAttributeRequest>{

	private static final byte RESERVED = 0;

	@Override
	public void write(final PaAttributeValueAttributeRequest data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueAttributeRequest aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		List<AttributeReference> attributes = aValue.getReferences();
		if(attributes != null && attributes.size() > 0){
			for (AttributeReference attributeReference : attributes) {
				/* reserved 8 bit(s) */
				buffer.write(RESERVED);
				
				/* vendor id 24 bit(s) */
				byte[] vendorId = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(attributeReference.getVendorId()).array(),5,8);
				try {
					buffer.write(vendorId);
				} catch (IOException e) {
					throw new SerializationException(
							"Vendor ID could not be written to the buffer.", e, false,
							Long.toString(attributeReference.getVendorId()));
				}
				
				/* type 32 bit(s) */
				byte[] type = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(attributeReference.getType()).array(),4,8);
				try {
					buffer.write(type);
				} catch (IOException e) {
					throw new SerializationException(
							"Type could not be written to the buffer.", e, false,
							Long.toString(attributeReference.getVendorId()));
				}
			}
		}else{
			throw new SerializationException("No attribute requests available, but there must be at least one.", false);
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
