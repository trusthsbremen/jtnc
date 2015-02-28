package org.ietf.nea.pb.serialize.reader.stream;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLengthEnum;

import de.hsbremen.tc.tnc.message.exception.RuleException;
import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.exception.ValidationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsReader;
import de.hsbremen.tc.tnc.message.util.ByteArrayHelper;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageAccessRecommendationValueReader implements TnccsReader<PbMessageValueAccessRecommendation>{

	private PbMessageValueAccessRecommendationBuilder baseBuilder;
	
	PbMessageAccessRecommendationValueReader(PbMessageValueAccessRecommendationBuilder builder){
		this.baseBuilder = builder;
	}
	
	@Override
	public PbMessageValueAccessRecommendation read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
	    
	    NotNull.check("Stream cannot be null.", in);
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PbMessageValueAccessRecommendation value = null;
		PbMessageValueAccessRecommendationBuilder builder = (PbMessageValueAccessRecommendationBuilder)this.baseBuilder.newInstance();

		try{
			
			try{
				
				int byteSize = 0;
				byte[] buffer = new byte[byteSize];
				
				/* ignore reserved */
				byteSize = 2;
				ByteArrayHelper.arrayFromStream(in, byteSize);
				errorOffset += byteSize;
				
				/* recommendation */
				byteSize = 2;
				buffer = ByteArrayHelper.arrayFromStream(in, byteSize);
				int code = ByteArrayHelper.toInt(buffer);
				builder.setRecommendation(code);
				errorOffset += byteSize;
				
			}catch (IOException e){
				throw new SerializationException(
						"Returned data for message value is to short or stream may be closed.", e, true);
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
