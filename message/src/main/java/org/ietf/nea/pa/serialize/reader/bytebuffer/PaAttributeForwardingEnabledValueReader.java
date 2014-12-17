package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueForwardingEnabled;
import org.ietf.nea.pa.attribute.PaAttributeValueForwardingEnabledBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeForwardingEnabledValueReader implements ImReader<PaAttributeValueForwardingEnabled>{

	private PaAttributeValueForwardingEnabledBuilder baseBuilder;
	
	PaAttributeForwardingEnabledValueReader(PaAttributeValueForwardingEnabledBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueForwardingEnabled read(final ByteBuffer buffer, final long attributeLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeValueForwardingEnabled value = null;
		PaAttributeValueForwardingEnabledBuilder builder = (PaAttributeValueForwardingEnabledBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* status */
				errorOffset = buffer.bytesRead();
				long status = buffer.readLong((byte)4);
				builder.setStatus(status);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueForwardingEnabled)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.FWD_EN.length();
	}

}
