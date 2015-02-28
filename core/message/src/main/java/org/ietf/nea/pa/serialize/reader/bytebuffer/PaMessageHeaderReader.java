package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;
import org.ietf.nea.pa.message.PaMessageHeader;
import org.ietf.nea.pa.message.PaMessageHeaderBuilder;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

public class PaMessageHeaderReader implements ImReader<PaMessageHeader> {

	private PaMessageHeaderBuilder baseBuilder;
	
	PaMessageHeaderReader(PaMessageHeaderBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaMessageHeader read(ByteBuffer buffer, long length)
			throws SerializationException, ValidationException{

		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaMessageHeader messageHeader = null;
		PaMessageHeaderBuilder builder = (PaMessageHeaderBuilder) this.baseBuilder.newInstance();
		
		try{
			try{
				
				
				/* version */
				errorOffset = buffer.bytesRead();
				byte version = buffer.readByte();
				builder.setVersion(version);

				/* ignore reserved */
				errorOffset = buffer.bytesRead();
				buffer.read(3);
				
				/* identifier */
				errorOffset = buffer.bytesRead();
				long identifier = buffer.readLong((byte)4);
				builder.setIdentifier(identifier);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			messageHeader = (PaMessageHeader)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return messageHeader;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.MESSAGE.length();
	}
}
