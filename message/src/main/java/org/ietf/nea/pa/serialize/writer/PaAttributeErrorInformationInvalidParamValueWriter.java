package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationInvalidParam;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributeErrorInformationInvalidParamValueWriter implements ImWriter<PaAttributeValueErrorInformationInvalidParam>{

	private PaMessageHeaderWriter writer;
	
	PaAttributeErrorInformationInvalidParamValueWriter(
			PaMessageHeaderWriter writer) {
		this.writer = writer;
	}

	@Override
	public void write(final PaAttributeValueErrorInformationInvalidParam data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueErrorInformationInvalidParam aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* message header copy 64 bit(s) */
		this.writer.write(aValue.getMessageHeader(), out);
		
		/* offset 32 bit(s) */
		byte[] offset = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(aValue.getOffset()).array(),4,8);
		try {
			buffer.write(offset);
		} catch (IOException e) {
			throw new SerializationException(
					"Offset could not be written to the buffer.", e, false,
					Long.toString(aValue.getOffset()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
