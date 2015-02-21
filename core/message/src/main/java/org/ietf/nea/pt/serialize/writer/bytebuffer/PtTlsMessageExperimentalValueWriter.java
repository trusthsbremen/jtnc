package org.ietf.nea.pt.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pt.value.PtTlsMessageValueExperimental;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PtTlsMessageExperimentalValueWriter implements TransportWriter<PtTlsMessageValueExperimental>{

	@Override
	public void write(final PtTlsMessageValueExperimental data, ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message header cannot be null.", data);
		
		NotNull.check("Buffer cannot be null.", buffer);
		
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
