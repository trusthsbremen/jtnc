package org.ietf.nea.pb.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.batch.PbBatchHeader;
import org.ietf.nea.pb.batch.PbBatchHeaderBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PbBatchHeaderReader implements TnccsReader<PbBatchHeader>{

	private PbBatchHeaderBuilder builder;
	
	PbBatchHeaderReader(PbBatchHeaderBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PbBatchHeader read(final InputStream in, final long batchLength)
			throws SerializationException, ValidationException{
		
				// ignore any given length and find out on your own.
		
				long errorOffset = 0;
				
				PbBatchHeader batchHeader = null;
				builder = (PbBatchHeaderBuilder)builder.clear();
				
				try{
					try{
						
						int byteSize = 0;
						byte[] buffer = new byte[byteSize];
						
						/* version */
						byteSize = 1;
						buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
						builder.setVersion(buffer[0]);
						errorOffset += byteSize;
						
						/* direction */
						byteSize = 1;
						buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
						byte directionality = (byte) ((buffer[0] & 0x80) >>> 7);
						builder.setDirection(directionality);
						errorOffset += byteSize;
	
						/* ignore reserved */
						byteSize = 1;
						ByteArrayHelper.arrayFromStream(in, byteSize);
						errorOffset += byteSize;
						
						/* type */
						byteSize = 1;
						buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
						byte type = (byte)(buffer[0] & 0x0F); 
						builder.setType(type);
						errorOffset += byteSize;
						
						/* length */
						byteSize = 4;
						buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
						long length = ByteArrayHelper.toLong(buffer);
						builder.setLength(length);
						errorOffset += byteSize;
						
					}catch (IOException e){
						throw new SerializationException("Returned data for batch header is to short or stream may be closed.",e,true);
					}

					batchHeader = (PbBatchHeader)builder.toBatchHeader();
					
				}catch (RuleException e){
					throw new ValidationException(e.getMessage(), e, errorOffset);
				}
				
				return batchHeader;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLength.BATCH.length();
	}

}
