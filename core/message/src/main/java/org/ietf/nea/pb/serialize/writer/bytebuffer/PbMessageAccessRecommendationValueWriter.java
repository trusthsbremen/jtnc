package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pb.message.PbMessageValueAccessRecommendation;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageAccessRecommendationValueWriter implements TnccsWriter<PbMessageValueAccessRecommendation>{

	private static final short RESERVED  = 0;
	
	@Override
	public void write(final PbMessageValueAccessRecommendation data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueAccessRecommendation mValue = data;
		try{
			/* reserved */
			buffer.writeShort(RESERVED);
			
			/* recommendation */
			buffer.writeUnsignedShort(mValue.getRecommendation().number());
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
