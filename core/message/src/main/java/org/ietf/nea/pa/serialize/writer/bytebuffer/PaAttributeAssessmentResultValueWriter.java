package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PaAttributeAssessmentResultValueWriter implements ImWriter<PaAttributeValueAssessmentResult>{

	@Override
	public void write(final PaAttributeValueAssessmentResult data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Value cannot be null.", data);
		
		NotNull.check("Buffer cannot be null.", buffer);
		
		PaAttributeValueAssessmentResult aValue = data;
		
		try{
			
			/* result */
			buffer.writeUnsignedInt(aValue.getResult().id());
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}
}
