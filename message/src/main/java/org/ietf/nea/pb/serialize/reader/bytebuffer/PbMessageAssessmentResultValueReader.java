package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResultBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PbMessageAssessmentResultValueReader implements TnccsReader<PbMessageValueAssessmentResult>{

	private PbMessageValueAssessmentResultBuilder baseBuilder;
	
	PbMessageAssessmentResultValueReader(PbMessageValueAssessmentResultBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageValueAssessmentResult read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PbMessageValueAssessmentResult value = null;
		PbMessageValueAssessmentResultBuilder builder = (PbMessageValueAssessmentResultBuilder)this.baseBuilder.newInstance();

		try{
			
			try{

				/* result 32 bit(s)*/
				errorOffset = buffer.bytesRead();
				long code = buffer.readLong((byte)4);
				builder.setResult(code);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PbMessageValueAssessmentResult)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLengthEnum.ASS_RES_VALUE.length();
	}

}
