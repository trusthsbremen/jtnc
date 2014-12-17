package org.ietf.nea.pb.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResultBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;

class PbMessageAssessmentResultValueReader implements TnccsReader<PbMessageValueAssessmentResult>{

	private PbMessageValueAssessmentResultBuilder baseBuilder;
	
	PbMessageAssessmentResultValueReader(PbMessageValueAssessmentResultBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageValueAssessmentResult read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PbMessageValueAssessmentResult value = null;
		PbMessageValueAssessmentResultBuilder builder = (PbMessageValueAssessmentResultBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* result */
				byteSize = 4;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				long code = ByteArrayHelper.toLong(buffer);
				builder.setResult(code);
				errorOffset += byteSize;
				
			}catch (IOException e){
				throw new SerializationException(
						"Returned data for message value is to short or stream may be closed.", e, true);
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
