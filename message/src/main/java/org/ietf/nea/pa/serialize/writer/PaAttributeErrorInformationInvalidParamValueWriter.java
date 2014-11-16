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

	@Override
	public void write(final PaAttributeValueErrorInformationInvalidParam data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueErrorInformationInvalidParam aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* message header copy 64 bit(s) */
		/* copy version 8 bit(s) */
		buffer.write(data.getMessageHeader().getVersion());

		/* copy reserved 24 bit(s) */
		try {
			buffer.write(data.getMessageHeader().getReserved());
		} catch (IOException e) {
			throw new SerializationException("Reserved space could not be written to the buffer.",e,false);
		}

		/* copy identifier 32 bit(s) */
		byte[] identifier = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(data.getMessageHeader().getIdentifier())
								.array(), 4, 8);
		try {
			buffer.write(identifier);
		} catch (IOException e) {
			throw new SerializationException("Idenitfier could not be written to the buffer.",e,false);
		}
		
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
