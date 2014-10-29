package org.ietf.nea.pb.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;

import de.hsbremen.tc.tnc.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;

class PbMessageAccessRecommendationValueWriter implements TnccsWriter<PbMessageValueAccessRecommendation>{

	private static final short RESERVED  = 0;
	
	@Override
	public void write(final PbMessageValueAccessRecommendation data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
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
		byte[] code = ByteBuffer.allocate(2).putShort(mValue.getRecommendation().number()).array();
		try {
			buffer.write(code);
		} catch (IOException e) {
			throw new SerializationException(
					"Recommendation code could not be written to the buffer.", e, false,
					Short.toString(mValue.getRecommendation().number()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
