package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageHeader;
import org.ietf.nea.pb.message.PbMessageHeaderBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PbMessageHeaderReader implements TnccsReader<PbMessageHeader>{

	private PbMessageHeaderBuilder baseBuilder;
	
	PbMessageHeaderReader(PbMessageHeaderBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageHeader read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PbMessageHeader messageHeader = null;
		PbMessageHeaderBuilder builder = (PbMessageHeaderBuilder)this.baseBuilder.newInstance();

		try{
			try{
				
				/* flags 8 bit(s)*/
				errorOffset = buffer.bytesRead();
				builder.setFlags(buffer.readByte());
				
				/* vendor ID 24 bit(s)*/
				errorOffset = buffer.bytesRead();
				long vendorId = buffer.readLong((byte)3);
				builder.setVendorId(vendorId);
				
				/* message type 32 bit(s)*/
				errorOffset = buffer.bytesRead();
				long messageType = buffer.readLong((byte)4);
				builder.setType(messageType);
				
				/* message length 32 bit(s) */
				errorOffset = buffer.bytesRead();
				long length = buffer.readLong((byte)4);
				builder.setLength(length);
				
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			messageHeader = (PbMessageHeader)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return messageHeader;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLengthEnum.MESSAGE.length();
	}

}
