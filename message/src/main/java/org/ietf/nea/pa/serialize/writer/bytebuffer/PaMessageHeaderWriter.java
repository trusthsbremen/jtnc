package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pa.message.PaMessageHeader;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PaMessageHeaderWriter implements ImWriter<PaMessageHeader>{

	private static final byte[] RESERVED = new byte[]{0,0,0};
	
	@Override
	public void write(final PaMessageHeader data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message header cannot be null.", data);
		
		NotNull.check("Buffer cannot be null.", buffer);

		PaMessageHeader mHead = data;
		try{
			/* version 8 bit(s) */
			buffer.writeUnsignedByte(mHead.getVersion());
	
			/* reserved */
			buffer.write(RESERVED);
	
			/* identifier 32 bit(s) */
			buffer.writeUnsignedInt(mHead.getIdentifier());
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
