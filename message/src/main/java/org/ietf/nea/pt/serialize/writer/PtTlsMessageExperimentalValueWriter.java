package org.ietf.nea.pt.serialize.writer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pt.value.PtTlsMessageValueExperimental;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageExperimentalValueWriter implements TransportWriter<PtTlsMessageValueExperimental>{

	@Override
	public void write(final PtTlsMessageValueExperimental data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PtTlsMessageValueExperimental mValue = data;

		try{
	
			/* message */
			buffer.write(mValue.getMessage().getBytes(Charset.forName("UTF-8")));

		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
