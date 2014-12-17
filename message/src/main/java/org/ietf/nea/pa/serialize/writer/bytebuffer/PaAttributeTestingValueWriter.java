package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueTesting;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeTestingValueWriter implements ImWriter<PaAttributeValueTesting>{

	@Override
	public void write(final PaAttributeValueTesting data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueTesting aValue = data;

		/* content String*/
		try{
			
			buffer.write(aValue.getContent().getBytes(Charset.forName("UTF-8")));
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
