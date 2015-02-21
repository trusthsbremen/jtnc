package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.ietf.nea.pa.attribute.PaAttributeValueOperationalStatus;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;
import de.hsbremen.tc.tnc.util.NotNull;

class PaAttributeOperationalStatusValueWriter implements ImWriter<PaAttributeValueOperationalStatus>{

	private static final byte[] RESERVED = new byte[]{0,0};
	
	private SimpleDateFormat dateFormater;
	
	PaAttributeOperationalStatusValueWriter() {
		this.dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		this.dateFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public void write(final PaAttributeValueOperationalStatus data, final OutputStream out)
			throws SerializationException {
		NotNull.check("Value cannot be null.", data);
		
		PaAttributeValueOperationalStatus aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* status 8 bit(s) */
		buffer.write(aValue.getStatus().status());
		
		/* result 8 bit(s) */
		buffer.write(aValue.getResult().result());
		
		/* reserved 24 bit(s) */
		try {
			buffer.write(RESERVED);
		} catch (IOException e) {
			throw new SerializationException("Reserved space could not be written to the buffer.",e,false);
		}
		
		/* last use 160 bit(s) */
		byte [] lastUse = this.dateFormater.format(aValue.getLastUse()).getBytes(Charset.forName("US-ASCII"));
		
		try {
			buffer.write(lastUse);
		} catch (IOException e) {
			throw new SerializationException("Last use date could not be written to the buffer.",e,false, aValue.getLastUse().toString());
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
