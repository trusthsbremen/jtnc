package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueTesting;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributeTestingValueWriter implements ImWriter<PaAttributeValueTesting>{

	@Override
	public void write(final PaAttributeValueTesting data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueTesting aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* content String*/
		try{
			buffer.write(aValue.getContent().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Content could not be written to the buffer.", e, false,
					aValue.getContent());
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
