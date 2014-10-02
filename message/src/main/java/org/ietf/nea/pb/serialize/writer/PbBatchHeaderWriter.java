package org.ietf.nea.pb.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pb.batch.PbBatchHeader;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;

public class PbBatchHeaderWriter implements TnccsWriter<PbBatchHeader>{

	private static final byte RESERVED = 0;
	
	@Override
	public void write(PbBatchHeader data, OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Batch can not be NULL.");
		}

		PbBatchHeader batch = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		/* Version 8 bit(s) */
		buffer.write(batch.getVersion());

		/* Direction 1 bit(s) + Reserved 7 bit(s) */
		buffer.write((byte) (batch.getDirectionality().directionality() << 7 | RESERVED));
		
		/* Reserved 8 bit(s) */
		buffer.write(RESERVED);
		
		/* Reserved 4 bit(s) + Type 4 bit(s) */
		buffer.write((byte) (RESERVED | (batch
				.getType().type() & 0x0F)));

		/* Length 32 bit(s) */
		byte[] length = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(batch.getLength()).array(),4,8);

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
