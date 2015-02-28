package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeHeader;
import org.ietf.nea.pa.attribute.PaAttributeHeaderBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeHeaderReader implements ImReader<PaAttributeHeader>{

	private PaAttributeHeaderBuilder baseBuilder;
	
	PaAttributeHeaderReader(PaAttributeHeaderBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeHeader read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeHeader messageHeader = null;
		PaAttributeHeaderBuilder builder = (PaAttributeHeaderBuilder)this.baseBuilder.newInstance();

		try{
			try{
				
				/* flags */
				errorOffset = buffer.bytesRead();
				builder.setFlags(buffer.readByte());
				
				/* vendor ID */
				errorOffset = buffer.bytesRead();
				long vendorId = buffer.readLong((byte)3);
				builder.setVendorId(vendorId);
				
				/* attribute type */
				errorOffset = buffer.bytesRead();
				long messageType = buffer.readLong((byte)4);
				builder.setType(messageType);
				
				/* attribute length */
				errorOffset = buffer.bytesRead();
				long length = buffer.readLong((byte)4);
				builder.setLength(length);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			messageHeader = (PaAttributeHeader)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return messageHeader;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.ATTRIBUTE.length();
	}

}
