package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueNumericVersion;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeNumericVersionValueWriter implements ImWriter<PaAttributeValueNumericVersion>{

	@Override
	public void write(final PaAttributeValueNumericVersion data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueNumericVersion aValue = data;
		
		try{

			/* major version 32 bit(s) */
			buffer.writeUnsignedInt(aValue.getMajorVersion());
			
			/* minor version 32 bit(s) */
			buffer.writeUnsignedInt(aValue.getMinorVersion());
			
			/* build number 32 bit(s) */
			buffer.writeUnsignedInt(aValue.getBuildVersion());
			
			/* service pack major 16 bit(s) */
			buffer.writeUnsignedShort(aValue.getServicePackMajor());
			
			/* service pack minor 16 bit(s) */
			buffer.writeUnsignedShort(aValue.getServicePackMinor());
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
