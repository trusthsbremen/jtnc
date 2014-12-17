package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeStringVersionValueWriter implements ImWriter<PaAttributeValueStringVersion>{

	@Override
	public void write(final PaAttributeValueStringVersion data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueStringVersion aValue = data;
		
		try{
			/* version length 8 bit(s)*/
			buffer.writeUnsignedByte(aValue.getVersionNumberLength());
		
			/* version String*/
			buffer.write(aValue.getVersionNumber().getBytes(Charset.forName("UTF-8")));
		
			/* build length 8 bit(s)*/
			buffer.writeUnsignedByte(aValue.getBuildVersionLength());
		
			/* build String */
			buffer.write(aValue.getBuildVersion().getBytes(Charset.forName("UTF-8")));

			/* config length 8 bit(s)*/
			buffer.writeUnsignedByte(aValue.getConfigurationVersionLength());
		
			/* config String */
			buffer.write(aValue.getConfigurationVersion().getBytes(Charset.forName("UTF-8")));
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
