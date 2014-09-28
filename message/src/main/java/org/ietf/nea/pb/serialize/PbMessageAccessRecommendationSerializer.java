package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;
import org.ietf.nea.pb.message.PbMessageValueAccessRecommendationBuilder;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.exception.ValidationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

public class PbMessageAccessRecommendationSerializer implements TnccsSerializer<PbMessageValueAccessRecommendation> {

	private static final int MESSAGE_VALUE_FIXED_SIZE = 4;
	
	private PbMessageValueAccessRecommendationBuilder builder;
	
	public  PbMessageAccessRecommendationSerializer(PbMessageValueAccessRecommendationBuilder builder){
		this.builder = builder;
	}
	
	
	@Override
	public void encode(final PbMessageValueAccessRecommendation data, final OutputStream out)
			throws SerializationException {	

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* Reserved */
		byte[] reserved = ByteBuffer.allocate(2).putShort(data.getReserved()).array();
		try {
			buffer.write(reserved);
		} catch (IOException e) {
			throw new SerializationException(
					"Reserved space could not be written to the buffer.", e,
					Short.toString(data.getReserved()));
		}
		
		/* Access Recommendation */
		byte[] code = ByteBuffer.allocate(2).putShort(data.getRecommendation().number()).array();
		try {
			buffer.write(code);
		} catch (IOException e) {
			throw new SerializationException(
					"Recommendation code could not be written to the buffer.", e,
					Short.toString(data.getRecommendation().number()));
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e);
		}
	}

	@Override
	public PbMessageValueAccessRecommendation decode(final InputStream in, final long length) throws SerializationException, ValidationException {
		
		PbMessageValueAccessRecommendation value = null; 	
		builder.clear();
		// ignore any given length and find out on your own.

		byte[] buffer = new byte[MESSAGE_VALUE_FIXED_SIZE];

		int count = 0;
		// wait till data is available
		while (count == 0) {
			try {
				count = in.read(buffer);
			} catch (IOException e) {
				throw new SerializationException(
						"InputStream could not be read.", e);
			}
		}

		
		if (count >= MESSAGE_VALUE_FIXED_SIZE){

			/* Ignore Reserved */
			/* ... */
			/* Access Recommendation */
			short code = ByteArrayHelper.toShort(new byte[]{buffer[2], buffer[3]});
			
			builder.setRecommendation(code);
			value = (PbMessageValueAccessRecommendation)builder.toValue();

		} else {
			throw new SerializationException("Returned data length (" + count
					+ ") for message is to short or stream may be closed.",
					Integer.toString(count));
		}
		
		return value;
	}
}
