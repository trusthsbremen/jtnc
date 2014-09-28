package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchBuilder;
import org.ietf.nea.pb.exception.PbMessageUnknownException;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

public class PbBatchSerializer implements
		TnccsSerializer<PbBatch> {

	private static final int BATCH_HEAD_SIZE = 8; /* in byte */

	private TnccsSerializer<PbMessage> messageSerializer;
	
	private PbBatchBuilder builder;
	
	public PbBatchSerializer(PbBatchBuilder builder, TnccsSerializer<PbMessage> messageSerializer) {
		this.builder = builder; 
		this.messageSerializer = messageSerializer;
	}
	
	@Override
	public void encode(final PbBatch data, final OutputStream out)
			throws SerializationException {
		
		if(data == null){
			throw new NullPointerException("Batch can not be NULL.");
		}

		PbBatch batch = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		/* Version 8 bit(s) */
		buffer.write(batch.getVersion());

		/* Direction 1 bit(s) + Reserved 7 bit(s) */
		buffer.write((byte) (batch.getDirectionality().directionality() << 7 | (batch
				.getReserved() & 0x0007F000) >>> 12));
		
		/* Reserved 8 bit(s) */
		buffer.write((byte) ((batch.getReserved() & 0x00000FF0) >>> 4));
		
		/* Reserved 4 bit(s) + Type 4 bit(s) */
		buffer.write((byte) ((batch.getReserved() & 0x0000000F) << 4 | (batch
				.getType().type() & 0x0F)));

		/* Length 32 bit(s) */
		byte[] length = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(batch.getLength()).array(),4,8);

		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException("Batch length could not be written to the buffer.", e);
		}
		
		// make the first write with batch data
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Batch header could not be written to the OutputStream.",e);
		}
		
		/* PB messages */
		List<PbMessage> messages = batch.getMessages();
		
		for (PbMessage message : messages) {
			this.messageSerializer.encode(message, out);
		}
	}

	@Override
	public PbBatch decode(final InputStream in, final long length) throws SerializationException, ValidationException {

		// ignore any given length and find out on your own.
		long batchLength = 0L;
				
		builder = (PbBatchBuilder)builder.clear();
		
		byte[] buffer = new byte[0];
		try{
			buffer = ByteArrayHelper.arrayFromStream(in, BATCH_HEAD_SIZE);
		}catch(IOException e){
			throw new SerializationException("Batch header could not be read from the InputStream.", e);
		}

		/* Batch header values */
		if(buffer.length < BATCH_HEAD_SIZE){
			/* PbBatch must be of version 2 */
			builder.setVersion(buffer[0]);

			byte directionality = (byte) (buffer[1] >>> 7);
			builder.setDirection(directionality);

			/* ignore reserved and continue with type */
			byte type = (byte)(buffer[3] & 0x0F); 
			builder.setType(type);
			
			/* length */
			batchLength = ByteArrayHelper.toLong(Arrays.copyOfRange(buffer, 4, 8));
			
		}else{
			throw new SerializationException("Returned data length ("+buffer.length+") for batch is to short or stream may be closed.", Integer.toString(buffer.length));
		}

		/* PB messages */
		long messageLength = 0;
		for(long l = (batchLength - BATCH_HEAD_SIZE); l > 0; l -= messageLength){				
			try{
				PbMessage message = (PbMessage) this.messageSerializer.decode(in, length);
				builder.addMessage(message);
				messageLength = message.getLength();
			}catch(SerializationException e){
				Throwable t = e.getCause();
				if(t != null && t instanceof PbMessageUnknownException){
					// TODO log
					PbMessage pb = ((PbMessageUnknownException)t).getPbMessage();
					// get the length to continue message parsing.
					messageLength = (pb != null) ? pb.getLength() : 0;
				}else{
					// throw this message because it can not be handled here.
					throw e;
				}
			}
		}
		
		PbBatch batch = null;

		batch = (PbBatch)builder.toBatch();
		
		return batch;
	}
}
