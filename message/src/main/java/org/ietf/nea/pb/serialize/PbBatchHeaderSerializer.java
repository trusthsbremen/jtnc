package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.batch.PbBatchHeaderBuilder;
import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbBatchHeaderSerializer implements
		TnccsSerializer<PbBatchHeader> {

	private static final byte RESERVED = 0;
	
	private PbBatchHeaderBuilder builder;
	
	PbBatchHeaderSerializer(PbBatchHeaderBuilder builder) {
		this.builder = builder; 
	}
	
	@Override
	public void encode(final PbBatchHeader data, final OutputStream out)
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
			throw new SerializationException("Batch length could not be written to the buffer.",e,false,0);
		}
		
		// make the first write with batch data
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Batch header could not be written to the OutputStream.",e,true,0);
		}
	}

	@Override
	public PbBatchHeader decode(final InputStream in, final long length) throws SerializationException, RuleException {

		// ignore any given length and find out on your own.
		long batchLength = 0L;
				
		builder = (PbBatchHeaderBuilder)builder.clear();
		
		/* PbBatch must be of version 2 */
		byte[] buffer = new byte[0];
		
		try{
			/* version */
			buffer = ByteArrayHelper.arrayFromStream(in, 1);
			builder.setVersion(buffer[0]);

			/* direction */
			buffer = ByteArrayHelper.arrayFromStream(in, 1);
			byte directionality = (byte) ((buffer[0] & 0x80) >>> 7);
			builder.setDirection(directionality);

			/* ignore reserved */
			ByteArrayHelper.arrayFromStream(in, 1);
			
			/* type */
			buffer = ByteArrayHelper.arrayFromStream(in, 1);
			byte type = (byte)(buffer[0] & 0x0F); 
			builder.setType(type);

			/* length */
			buffer = ByteArrayHelper.arrayFromStream(in, 4);
			batchLength = ByteArrayHelper.toLong(buffer);
			builder.setLength(batchLength);
			
		}catch (IOException e){
			throw new SerializationException("Returned data for batch header is to short or stream may be closed.",true,0,Integer.toString(buffer.length));
		}

		PbBatchHeader batchHeader = null;

		batchHeader = (PbBatchHeader)builder.toBatchHeader();
		
		return batchHeader;
	}
}
