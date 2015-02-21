package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.util.PaAttributeValueErrorInformationUnsupportedVersion;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;
import de.hsbremen.tc.tnc.util.NotNull;

class PaAttributeErrorInformationUnsupportedVersionValueWriter implements ImWriter<PaAttributeValueErrorInformationUnsupportedVersion>{

	private static final byte[] RESERVED = new byte[]{0,0};

	@Override
	public void write(final PaAttributeValueErrorInformationUnsupportedVersion data, final OutputStream out)
			throws SerializationException {
		NotNull.check("Value cannot be null.", data);
		
		PaAttributeValueErrorInformationUnsupportedVersion aValue = data;
		
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
		
		
		/* max version 8 bit(s) */
		buffer.write(aValue.getMaxVersion());
		
		/* min version 8 bit(s) */
		buffer.write(aValue.getMinVersion());
		
		/* Reserved 16 bit(s) */
		try {
			buffer.write(RESERVED);
		} catch (IOException e) {
			throw new SerializationException("Reserved space could not be written to the buffer.",e,false);
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
