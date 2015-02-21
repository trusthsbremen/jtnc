package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageErrorParameterOffsetSubValueWriter implements TnccsWriter<PbMessageValueErrorParameterOffset>{

	@Override
	public void write(final PbMessageValueErrorParameterOffset data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueErrorParameterOffset mValue = data;

		try{
			/* Offset */
			buffer.writeUnsignedInt(mValue.getOffset());

		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
