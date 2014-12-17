package org.ietf.nea.pb.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsWriter;

class PbMessageAccessRecommendationValueWriter implements TnccsWriter<PbMessageValueAccessRecommendation>{

	private static final short RESERVED  = 0;
	
	@Override
	public void write(final PbMessageValueAccessRecommendation data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message value cannot be NULL.");
		}
		
		PbMessageValueAccessRecommendation mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* reserved */
		byte[] reserved = ByteBuffer.allocate(2).putShort(RESERVED).array();
		try {
			buffer.write(reserved);
		} catch (IOException e) {
			throw new SerializationException(
					"Reserved space could not be written to the buffer.", e, false,
					Short.toString(RESERVED));
		}
		
		/* recommendation */
		byte[] code = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(mValue.getRecommendation().number()).array(), 2,4);
		try {
			buffer.write(code);
		} catch (IOException e) {
			throw new SerializationException(
					"Recommendation code could not be written to the buffer.", e, false,
					Integer.toString(mValue.getRecommendation().number()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
