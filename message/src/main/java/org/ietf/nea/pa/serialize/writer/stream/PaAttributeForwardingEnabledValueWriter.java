package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.PaAttributeValueForwardingEnabled;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;

class PaAttributeForwardingEnabledValueWriter implements ImWriter<PaAttributeValueForwardingEnabled>{

	@Override
	public void write(final PaAttributeValueForwardingEnabled data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueForwardingEnabled aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* forwarding status */
		byte[] result = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(aValue.getForwardingStatus().status()).array(),4,8);
		try {
			buffer.write(result);
		} catch (IOException e) {
			throw new SerializationException(
					"Status code could not be written to the buffer.", e, false,
					Long.toString(aValue.getForwardingStatus().status()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}
}
