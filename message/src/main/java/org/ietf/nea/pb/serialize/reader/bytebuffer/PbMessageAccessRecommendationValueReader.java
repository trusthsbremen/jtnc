package org.ietf.nea.pb.serialize.reader.bytebuffer;

import java.nio.BufferUnderflowException;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PbMessageAccessRecommendationValueReader implements TnccsReader<PbMessageValueAccessRecommendation>{

	private PbMessageValueAccessRecommendationBuilder baseBuilder;
	
	PbMessageAccessRecommendationValueReader(PbMessageValueAccessRecommendationBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageValueAccessRecommendation read(final ByteBuffer buffer, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PbMessageValueAccessRecommendation value = null;
		PbMessageValueAccessRecommendationBuilder builder = (PbMessageValueAccessRecommendationBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				/* ignore reserved 16 bit(s) */
				errorOffset = buffer.bytesRead();
				buffer.read(2);
				
				/* recommendation 16 bit(s)*/
				errorOffset = buffer.bytesRead();
				int code = buffer.readInt((byte)2);
				builder.setRecommendation(code);
				
			}catch (BufferUnderflowException e){
				throw new SerializationException("Data length " +buffer.bytesWritten()+ " in buffer to short.",e,true, Long.toString(buffer.bytesWritten()));
			}

			value = (PbMessageValueAccessRecommendation)builder.toObject();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLengthEnum.ACC_REC_VALUE.length();
	}

}
