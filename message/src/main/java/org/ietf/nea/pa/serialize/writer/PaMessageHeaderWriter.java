package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pa.message.PaMessageHeader;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImWriter;

class PaMessageHeaderWriter implements ImWriter<PaMessageHeader>{

	private static final byte[] RESERVED = new byte[]{0,0,0};
	
	@Override
	public void write(final PaMessageHeader data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}

		PaMessageHeader mHead = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		/* version 8 bit(s) */
		buffer.write(mHead.getVersion());

		/* reserved 24 bit(s) */
		try {
			buffer.write(RESERVED);
		} catch (IOException e) {
			throw new SerializationException("Reserved space could not be written to the buffer.",e,false);
		}

		/* identifier 32 bit(s) */
		byte[] identifier = Arrays
				.copyOfRange(
						ByteBuffer.allocate(8).putLong(mHead.getIdentifier())
								.array(), 4, 8);
		
		try {
			buffer.write(identifier);
		} catch (IOException e) {
			throw new SerializationException("Identifier could not be written to the buffer.",e,false);
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message header could not be written to the OutputStream.",e,true);
		}
		
	}

}
