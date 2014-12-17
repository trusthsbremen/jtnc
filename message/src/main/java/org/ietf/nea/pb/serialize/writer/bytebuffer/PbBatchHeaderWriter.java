package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pb.batch.PbBatchHeader;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PbBatchHeaderWriter implements TnccsWriter<PbBatchHeader>{

	private static final byte RESERVED = 0;
	
	@Override
	public void write(final PbBatchHeader data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Batch header cannot be NULL.");
		}

		PbBatchHeader bHead = data;
	
		try{
			/* Version 8 bit(s) */
			buffer.writeUnsignedByte(bHead.getVersion());
	
			/* Direction 1 bit(s) + Reserved 7 bit(s) */
			buffer.writeByte((byte) (bHead.getDirectionality().toDirectionalityBit() << 7 | RESERVED));
			
			/* Reserved 8 bit(s) */
			buffer.writeByte(RESERVED);
			
			/* Reserved 4 bit(s) + Type 4 bit(s) */
			buffer.writeByte((byte) (RESERVED | (bHead
					.getType().type() & 0x0F)));
	
			/* Length 32 bit(s) */
			buffer.writeUnsignedInt(bHead.getLength());

		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
