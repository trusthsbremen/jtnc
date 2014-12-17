package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.batch.PbBatchHeaderBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PbBatchHeaderReader implements TnccsReader<PbBatchHeader>{

	private PbBatchHeaderBuilder baseBuilder;
	
	PbBatchHeaderReader(PbBatchHeaderBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbBatchHeader read(final ByteBuffer buffer, final long batchLength)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PbBatchHeader batchHeader = null;
				PbBatchHeaderBuilder builder = (PbBatchHeaderBuilder)this.baseBuilder.newInstance();
				
				try{
					try{
						
						/* version  8 bit(s)*/
						errorOffset = buffer.bytesRead();
						builder.setVersion(buffer.readByte());
						
						/* direction 1 bit(s) of byte direction + reserved*/
						errorOffset = buffer.bytesRead();
						byte directionality = (byte) ((buffer.readByte() & 0x80) >>> 7);
						builder.setDirection(directionality);
	
						/* ignore reserved 8 bit(s) */
						errorOffset = buffer.bytesRead();
						buffer.readByte();
						
						/* type 4 bit(s) of byte reserved + type*/
						errorOffset = buffer.bytesRead();
						byte type = (byte)(buffer.readByte() & 0x0F); 
						builder.setType(type);
						
						/* length 32 bit(s)*/
						errorOffset = buffer.bytesRead();
						long length = buffer.readLong((byte)4);
						builder.setLength(length);
						
						
					}catch (BufferUnderflowException e){
						throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
					}

					batchHeader = (PbBatchHeader)builder.toObject();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return batchHeader;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLengthEnum.BATCH.length();
	}

}
