package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.ietf.nea.pb.batch.PbBatch;
import org.ietf.nea.pb.batch.PbBatchBuilderIetf;
import org.ietf.nea.pb.batch.enums.PbBatchDirectionalityEnum;
import org.ietf.nea.pb.batch.enums.PbBatchTypeEnum;
import org.ietf.nea.pb.message.PbMessage;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

public class PbBatchSerializer implements
		TnccsSerializer<PbBatch> {

	private static final int BATCH_HEAD_SIZE = PbBatch.FIXED_LENGTH; /* in byte */

	private TnccsSerializer<PbMessage> messageHandler;
	
	public PbBatchSerializer(TnccsSerializer<PbMessage> messageHandler) {
		this.messageHandler = messageHandler;
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
				.getType().number() & 0x0F)));

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
			this.messageHandler.encode(message, out);
		}
	}

	@Override
	public PbBatch decode(final InputStream in, long length) throws SerializationException {

		PbBatchBuilderIetf builder = new PbBatchBuilderIetf();
		
		byte[] buffer = new byte[BATCH_HEAD_SIZE];
		int count = 0;
		/* wait until data is available */
		while(count == 0){
			try {
				count = in.read(buffer);
			} catch (IOException e) {
				throw new SerializationException("InputStream could not be read.",e);
			}
		}
		
		// ignore any given length and find out on your own.
		long batchLength = 0L;

		/* Batch header values */
		if(count >= BATCH_HEAD_SIZE){
			batchLength = createBatchHeader(builder,buffer);
		}else{
			throw new SerializationException("Returned data length ("+count+") for batch is to short or stream may be closed.", Integer.toString(count));
		}

		/* PB messages */
		long messageLength = 0;
		for(long l = batchLength - PbBatch.FIXED_LENGTH; l > 0; l -= messageLength){				
			messageLength = addMessageToBatch(builder, in, l);
		}
		
		return builder.toBatch();
	}

	private long createBatchHeader(final PbBatchBuilderIetf builder, final byte[] data) throws SerializationException{
		/* PbBatch must be of version 2 */
		if(data[0] != 2){
			throw new SerializationException("The Version #"+ data[0] +" is not supported!",Byte.toString(data[0]));
		}

		byte directionality = (byte) (data[1] >>> 7);
		builder.setBatchDirection( (directionality == PbBatchDirectionalityEnum.TO_PBC.directionality()) ? 
				PbBatchDirectionalityEnum.TO_PBC : PbBatchDirectionalityEnum.TO_PBS);

		/* ignore reserved and continue with type */
		byte type = (byte)(data[3] & 0x0F); 
		
		if (type == PbBatchTypeEnum.CDATA.number()){
			builder.setBatchType(PbBatchTypeEnum.CDATA);
		}else if (type == PbBatchTypeEnum.CRETRY.number()){
			builder.setBatchType(PbBatchTypeEnum.CRETRY);
		}else if (type == PbBatchTypeEnum.CLOSE.number()){
			builder.setBatchType(PbBatchTypeEnum.CLOSE);
		}else if (type == PbBatchTypeEnum.SDATA.number()){
			builder.setBatchType(PbBatchTypeEnum.SDATA);
		}else if (type == PbBatchTypeEnum.SRETRY.number()){
			builder.setBatchType(PbBatchTypeEnum.SRETRY);
		}else if (type == PbBatchTypeEnum.RESULT.number()){
			builder.setBatchType(PbBatchTypeEnum.RESULT);
		}else{
			throw new SerializationException("Batch type with #" + type +" is not supported.", Integer.toString(type));
		}
		
		/* length */

		long length = ByteArrayHelper.toLong(Arrays.copyOfRange(data, 4, 8));

		return length;
	}
	
	private long addMessageToBatch(final PbBatchBuilderIetf builder, final InputStream in, final long length) throws SerializationException {
		PbMessage message = (PbMessage) this.messageHandler.decode(in, length);
		builder.addMessage(message);
		return message.getLength();
	}
}
