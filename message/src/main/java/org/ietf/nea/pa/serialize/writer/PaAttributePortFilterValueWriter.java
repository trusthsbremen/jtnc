package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.ietf.nea.pa.attribute.PaAttributeValuePortFilter;
import org.ietf.nea.pa.attribute.util.PortFilterEntry;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.m.serialize.ImWriter;

class PaAttributePortFilterValueWriter implements ImWriter<PaAttributeValuePortFilter>{

	private static final byte RESERVED = 0;

	@Override
	public void write(final PaAttributeValuePortFilter data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValuePortFilter aValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		List<PortFilterEntry> entries = aValue.getFilterEntries();
		if(entries != null && entries.size() > 0){
			for (PortFilterEntry entry : entries) {
				
				/* reserved (7 bit(s)) + blocking status (1 bit(s)) = 8 bit(s) */
				buffer.write(RESERVED | (entry.getFilterStatus().toBlockedBit() & 0x01));
				
				/* protocol 8 bit(s) */
				buffer.write(entry.getProtocolNumber());

				/* port number 16 bit(s) */
				byte[] portNumber = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(entry.getProtocolNumber()).array(),2,4);
				try {
					buffer.write(portNumber);
				} catch (IOException e) {
					throw new SerializationException(
							"Port number could not be written to the buffer.", e, false,
							Integer.toString(entry.getPortNumber()));
				}
			}
		}else{
			throw new SerializationException("No port filter entry available, but there must be at least one.", false);
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
