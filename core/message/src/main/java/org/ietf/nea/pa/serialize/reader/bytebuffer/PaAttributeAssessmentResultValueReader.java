package org.ietf.nea.pa.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResult;
import org.ietf.nea.pa.attribute.PaAttributeValueAssessmentResultBuilder;
import org.ietf.nea.pa.attribute.enums.PaAttributeTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeAssessmentResultValueReader implements ImReader<PaAttributeValueAssessmentResult>{

	private PaAttributeValueAssessmentResultBuilder baseBuilder;
	
	PaAttributeAssessmentResultValueReader(PaAttributeValueAssessmentResultBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PaAttributeValueAssessmentResult read(final ByteBuffer buffer, final long attributeLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PaAttributeValueAssessmentResult value = null;
		PaAttributeValueAssessmentResultBuilder builder = (PaAttributeValueAssessmentResultBuilder)this.baseBuilder.newInstance();

		try{
			
			try{

				/* result */
				errorOffset = buffer.bytesRead();
				long code  = buffer.readLong((byte)4);
				builder.setResult(code);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PaAttributeValueAssessmentResult)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PaAttributeTlvFixedLengthEnum.ASS_RES.length();
	}

}
