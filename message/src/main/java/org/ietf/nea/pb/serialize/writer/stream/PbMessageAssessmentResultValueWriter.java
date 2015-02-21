package org.ietf.nea.pb.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsWriter;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageAssessmentResultValueWriter implements TnccsWriter<PbMessageValueAssessmentResult>{

	@Override
	public void write(final PbMessageValueAssessmentResult data, final OutputStream out)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueAssessmentResult mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* result */
		byte[] result = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(mValue.getResult().number()).array(),4,8);
		try {
			buffer.write(result);
		} catch (IOException e) {
			throw new SerializationException(
					"Result code could not be written to the buffer.", e, false,
					Long.toString(mValue.getResult().number()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
