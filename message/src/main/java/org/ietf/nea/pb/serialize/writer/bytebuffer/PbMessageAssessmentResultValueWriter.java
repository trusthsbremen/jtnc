package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pb.message.PbMessageValueAssessmentResult;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageAssessmentResultValueWriter implements TnccsWriter<PbMessageValueAssessmentResult>{

	@Override
	public void write(final PbMessageValueAssessmentResult data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueAssessmentResult mValue = data;

		try{
			/* result */
			buffer.writeUnsignedInt(mValue.getResult().number());
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
