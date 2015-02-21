package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageErrorParameterVersionSubValueWriter implements TnccsWriter<PbMessageValueErrorParameterVersion>{

	private static final byte RESERVED = 0;
	
	@Override
	public void write(final PbMessageValueErrorParameterVersion data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueErrorParameterVersion mValue = data;

		try{
			/* bad version */
			buffer.writeUnsignedByte(mValue.getBadVersion());
			
			/* max version */
			buffer.writeUnsignedByte(mValue.getMaxVersion());
			
			/* min version */
			buffer.writeUnsignedByte(mValue.getMinVersion());
			
			/* reserved */
			buffer.writeUnsignedByte(RESERVED);

		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
