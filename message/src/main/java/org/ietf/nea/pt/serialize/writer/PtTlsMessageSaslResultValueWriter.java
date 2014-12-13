package org.ietf.nea.pt.serialize.writer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pt.value.PtTlsMessageValueSaslResult;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageSaslResultValueWriter implements TransportWriter<PtTlsMessageValueSaslResult>{

	@Override
	public void write(final PtTlsMessageValueSaslResult data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PtTlsMessageValueSaslResult mValue = data;

		try{
	
			/* result 16 bit(s) */
			buffer.writeUnsignedShort(mValue.getResult().code());
			
			/* preferred version 8 bit(s) */
			if(mValue.getResultData() != null){
				buffer.write(mValue.getResultData());
			}
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
