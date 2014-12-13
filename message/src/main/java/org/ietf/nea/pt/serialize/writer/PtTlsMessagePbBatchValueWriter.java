package org.ietf.nea.pt.serialize.writer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pt.value.PtTlsMessageValuePbBatch;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessagePbBatchValueWriter implements TransportWriter<PtTlsMessageValuePbBatch>{

	@Override
	public void write(final PtTlsMessageValuePbBatch data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PtTlsMessageValuePbBatch mValue = data;

		try{
	
			/* message */
			buffer.write(mValue.getTnccsData());
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
