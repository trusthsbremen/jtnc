package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.PaAttributeValueStringVersion;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;
import de.hsbremen.tc.tnc.util.NotNull;

class PaAttributeStringVersionValueWriter implements ImWriter<PaAttributeValueStringVersion>{

	@Override
	public void write(final PaAttributeValueStringVersion data, final OutputStream out)
			throws SerializationException {
		NotNull.check("Value cannot be null.", data);
		
		PaAttributeValueStringVersion aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* version length 8 bit(s)*/
		buffer.write(aValue.getVersionNumberLength());
		
		/* version String*/
		try{
			buffer.write(aValue.getVersionNumber().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Version number could not be written to the buffer.", e, false,
					aValue.getVersionNumber());
		}
		
		/* build length 8 bit(s)*/
		buffer.write(aValue.getBuildVersionLength());
		
		/* build String */
		try{
			buffer.write(aValue.getBuildVersion().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Build version could not be written to the buffer.", e, false,
					aValue.getBuildVersion());
		}

		/* config length 8 bit(s)*/
		buffer.write(aValue.getConfigurationVersionLength());
		
		/* config String */
		try{
			buffer.write(aValue.getConfigurationVersion().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Configuration version could not be written to the buffer.", e, false,
					aValue.getConfigurationVersion());
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
