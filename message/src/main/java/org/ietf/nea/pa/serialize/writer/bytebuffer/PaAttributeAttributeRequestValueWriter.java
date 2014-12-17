package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValueAttributeRequest;
import org.ietf.nea.pa.attribute.util.AttributeReference;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeAttributeRequestValueWriter implements ImWriter<PaAttributeValueAttributeRequest>{

	private static final byte RESERVED = 0;

	@Override
	public void write(final PaAttributeValueAttributeRequest data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueAttributeRequest aValue = data;

		List<AttributeReference> attributes = aValue.getReferences();
		try{
			if(attributes != null && attributes.size() > 0){
				for (AttributeReference attributeReference : attributes) {
					/* reserved 8 bit(s) */
					buffer.writeByte(RESERVED);
					
					/* vendor id 24 bit(s) */
					buffer.writeDigits(attributeReference.getVendorId(),(byte)3);
				
					/* type 32 bit(s) */
					buffer.writeUnsignedInt(attributeReference.getType());
				}
			}else{
				throw new SerializationException("No attribute requests available, but there must be at least one.", false);
			}
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
