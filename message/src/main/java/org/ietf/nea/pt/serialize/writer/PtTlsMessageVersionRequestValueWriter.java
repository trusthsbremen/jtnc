package org.ietf.nea.pt.serialize.writer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pt.value.PtTlsMessageValueVersionRequest;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageVersionRequestValueWriter implements TransportWriter<PtTlsMessageValueVersionRequest>{

	@Override
	public void write(final PtTlsMessageValueVersionRequest data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PtTlsMessageValueVersionRequest mValue = data;

		try{
	
			/* reserved 8 bit(s) */
			buffer.writeByte((byte)0);
			
			/* min version 8b bit(s) */
			buffer.writeUnsignedByte(mValue.getMinVersion());
			
			/* max version 8 bit(s) */
			buffer.writeUnsignedByte(mValue.getMaxVersion());
			
			/* preferred version 8 bit(s) */
			buffer.writeUnsignedByte(mValue.getPreferredVersion());
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
