package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilder;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageAccessRecommendationSerializer implements TnccsSerializer<PbMessageValueAccessRecommendation> {

	//private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageTlvFixedLength.ACC_REC_VALUE.length();
	
	private PbMessageValueAccessRecommendationBuilder builder;
	
	PbMessageAccessRecommendationSerializer(PbMessageValueAccessRecommendationBuilder builder){
		this.builder = builder;
	}
	
	
	@Override
	public void encode(final PbMessageValueAccessRecommendation data, final OutputStream out)
			throws SerializationException {	

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* reserved */
		byte[] reserved = ByteBuffer.allocate(2).putShort(data.getReserved()).array();
		try {
			buffer.write(reserved);
		} catch (IOException e) {
			throw new SerializationException(
					"Reserved space could not be written to the buffer.", e, false, 0,
					Short.toString(data.getReserved()));
		}
		
		/* recommendation */
		byte[] code = ByteBuffer.allocate(2).putShort(data.getRecommendation().number()).array();
		try {
			buffer.write(code);
		} catch (IOException e) {
			throw new SerializationException(
					"Recommendation code could not be written to the buffer.", e, false, 0,
					Short.toString(data.getRecommendation().number()));
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true, 0);
		}
	}

	@Override
	public PbMessageValueAccessRecommendation decode(final InputStream in, final long length) throws SerializationException, RuleException {
		
		PbMessageValueAccessRecommendation value = null; 	
		builder.clear();
		
		// ignore any given length and find out on your own.

		byte[] buffer = new byte[0];

		try{
			/* ignore reserved */
			ByteArrayHelper.arrayFromStream(in, 2);
			
			/* recommendation */
			buffer = ByteArrayHelper.arrayFromStream(in, 2);
			short code = ByteArrayHelper.toShort(buffer);
			builder.setRecommendation(code);
			
		}catch(IOException e){
			throw new SerializationException(
						"Returned data for message value is to short or stream may be closed.", e, true, 0, Integer.toString(buffer.length));
		}
		
		value = (PbMessageValueAccessRecommendation)builder.toValue();
		
		return value;
	}
}
