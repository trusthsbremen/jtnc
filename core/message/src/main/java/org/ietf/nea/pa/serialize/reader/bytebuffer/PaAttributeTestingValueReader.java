package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;
import java.nio.charset.Charset;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueTesting;
import org.ietf.nea.pa.attribute.PaAttributeValueTestingBuilder;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeTestingValueReader implements ImReader<PaAttributeValueTesting>{

	private PaAttributeValueTestingBuilder baseBuilder;
	
	PaAttributeTestingValueReader(PaAttributeValueTestingBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueTesting read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		long errorOffset = 0;
		
		PaAttributeValueTesting value = null;
		PaAttributeValueTestingBuilder builder = (PaAttributeValueTestingBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* content */
				errorOffset = buffer.bytesRead();
				// FIXME this could lead to trouble because java can only handle a String of Integer.MAX_VALUE 
				// the length is shortened here and may switch to a negative value.
				byte[] sData = buffer.read((int)messageLength); 
				String content = new String(sData, Charset.forName("UTF-8"));
				builder.setContent(content);
			
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueTesting)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}


	@Override
	public byte getMinDataLength() {
	
		// no minimal length
		return 0; 
	}
}
