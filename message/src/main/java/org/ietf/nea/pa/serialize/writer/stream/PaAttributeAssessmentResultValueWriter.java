package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;

class PaAttributeAssessmentResultValueWriter implements ImWriter<PaAttributeValueAssessmentResult>{

	@Override
	public void write(final PaAttributeValueAssessmentResult data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueAssessmentResult aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* result */
		byte[] result = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(aValue.getResult().number()).array(),4,8);
		try {
			buffer.write(result);
		} catch (IOException e) {
			throw new SerializationException(
					"Result code could not be written to the buffer.", e, false,
					Long.toString(aValue.getResult().number()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}
}
