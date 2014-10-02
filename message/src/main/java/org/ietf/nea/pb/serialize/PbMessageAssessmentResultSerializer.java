package org.ietf.nea.pb.serialize;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pb.exception.RuleException;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;
import org.ietf.nea.pb.message.PbMessageValueAssessmentResultBuilder;
import org.ietf.nea.pb.serialize.util.ByteArrayHelper;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsSerializer;

class PbMessageAssessmentResultSerializer implements TnccsSerializer<PbMessageValueAssessmentResult> {

	//private static final int MESSAGE_VALUE_FIXED_SIZE = PbMessageTlvFixedLength.ASS_RES_VALUE.length();
	
	private PbMessageValueAssessmentResultBuilder builder;
	
	PbMessageAssessmentResultSerializer(PbMessageValueAssessmentResultBuilder builder){
	    this.builder = builder;
	}
	
	
	@Override
	public void encode(final PbMessageValueAssessmentResult data, final OutputStream out)
			throws SerializationException {	

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* result */
		byte[] result = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(data.getResult().number()).array(),4,8);
		try {
			buffer.write(result);
		} catch (IOException e) {
			throw new SerializationException(
					"Result code could not be written to the buffer.", e, false, 0,
					Long.toString(data.getResult().number()));
		}
		
		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true, 0);
		}
	}

	@Override
	public PbMessageValueAssessmentResult decode(final InputStream in, final long length) throws SerializationException, RuleException {
		PbMessageValueAssessmentResult value = null; 	
		this.builder.clear();
		
		// ignore any given length and find out on your own.

		byte[] buffer = new byte[0];

		try{
			/* result */
			buffer = ByteArrayHelper.arrayFromStream(in, 4);
			long code = ByteArrayHelper.toLong(buffer);
			this.builder.setResult(code);
			
		}catch(IOException e){
			throw new SerializationException(
						"Returned data for message value is to short or stream may be closed.", e, true, 0, Integer.toString(buffer.length));
			
		}
		
		value = (PbMessageValueAssessmentResult)this.builder.toValue();

		return value;
	}
}
