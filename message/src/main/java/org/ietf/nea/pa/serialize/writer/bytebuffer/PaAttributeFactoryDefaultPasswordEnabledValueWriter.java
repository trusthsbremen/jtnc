package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueFactoryDefaultPasswordEnabled;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeFactoryDefaultPasswordEnabledValueWriter implements ImWriter<PaAttributeValueFactoryDefaultPasswordEnabled>{

	@Override
	public void write(final PaAttributeValueFactoryDefaultPasswordEnabled data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueFactoryDefaultPasswordEnabled aValue = data;
		
		try{

			/* factory default password status */
			buffer.writeUnsignedInt(aValue.getFactoryDefaultPasswordStatus().status());
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}
}
