package org.ietf.nea.pb.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pb.batch.PbBatchHeader;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsWriter;

class PbBatchHeaderWriter implements TnccsWriter<PbBatchHeader>{

	private static final byte RESERVED = 0;
	
	@Override
	public void write(final PbBatchHeader data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Batch header cannot be NULL.");
		}

		PbBatchHeader bHead = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		/* Version 8 bit(s) */
		buffer.write(bHead.getVersion());

		/* Direction 1 bit(s) + Reserved 7 bit(s) */
		buffer.write((byte) (bHead.getDirectionality().toDirectionalityBit() << 7 | RESERVED));
		
		/* Reserved 8 bit(s) */
		buffer.write(RESERVED);
		
		/* Reserved 4 bit(s) + Type 4 bit(s) */
		buffer.write((byte) (RESERVED | (bHead
				.getType().type() & 0x0F)));

		/* Length 32 bit(s) */
		byte[] length = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(bHead.getLength()).array(),4,8);

		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException("Batch length could not be written to the buffer.",e,false);
		}
		
		// make the first write with batch data
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Batch header could not be written to the OutputStream.",e,true);
		}
		
	}

}
