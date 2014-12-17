package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pa.attribute.PaAttributeValueFactoryDefaultPasswordEnabled;
import org.ietf.nea.pa.attribute.PaAttributeValueFactoryDefaultPasswordEnabledBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeFactoryDefaultPasswordEnabledValueReader implements ImReader<PaAttributeValueFactoryDefaultPasswordEnabled>{

	private PaAttributeValueFactoryDefaultPasswordEnabledBuilder baseBuilder;
	
	PaAttributeFactoryDefaultPasswordEnabledValueReader(PaAttributeValueFactoryDefaultPasswordEnabledBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueFactoryDefaultPasswordEnabled read(final ByteBuffer buffer, final long attributeLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeValueFactoryDefaultPasswordEnabled value = null;
		PaAttributeValueFactoryDefaultPasswordEnabledBuilder builder = 
				(PaAttributeValueFactoryDefaultPasswordEnabledBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* status */
				errorOffset = buffer.bytesRead();
				long status = buffer.readLong((byte)4);
				builder.setStatus(status);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueFactoryDefaultPasswordEnabled)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.FAC_PW.length();
	}

}
