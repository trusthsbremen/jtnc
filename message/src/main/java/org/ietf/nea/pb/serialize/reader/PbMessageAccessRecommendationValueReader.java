package org.ietf.nea.pb.serialize.reader;

import java.io.IOException;
import java.io.InputStream;

import org.ietf.nea.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilder;
import org.ietf.nea.pb.message.enums.PbMessageTlvFixedLength;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsReader;
import de.hsbremen.tc.tnc.util.ByteArrayHelper;

class PbMessageAccessRecommendationValueReader implements TnccsReader<PbMessageValueAccessRecommendation>{

	private PbMessageValueAccessRecommendationBuilder builder;
	
	PbMessageAccessRecommendationValueReader(PbMessageValueAccessRecommendationBuilder builder){
		this.builder = builder;
	}
	
	@Override
	public PbMessageValueAccessRecommendation read(final InputStream in, final long messageLength)
			throws SerializationException, ValidationException {
		
		// ignore any given length and find out on your own.
		
		long errorOffset = 0;
		
		PbMessageValueAccessRecommendation value = null;
		builder = (PbMessageValueAccessRecommendationBuilder)builder.clear();

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

			value = (PbMessageValueAccessRecommendation)builder.toValue();
			
		}catch (RuleException e){
			throw new ValidationException(e.getMessage(), e, errorOffset);
		}

		return value;
	}

	@Override
	public byte getMinDataLength() {
		return PbMessageTlvFixedLength.ACC_REC_VALUE.length();
	}

}
