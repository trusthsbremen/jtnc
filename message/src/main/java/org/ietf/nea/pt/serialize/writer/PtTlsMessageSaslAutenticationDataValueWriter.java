package org.ietf.nea.pt.serialize.writer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pt.value.PtTlsMessageValueSaslAuthenticationData;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageSaslAutenticationDataValueWriter implements TransportWriter<PtTlsMessageValueSaslAuthenticationData>{

	@Override
	public void write(final PtTlsMessageValueSaslAuthenticationData data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PtTlsMessageValueSaslAuthenticationData mValue = data;

		try{
	
			/* message */
			buffer.write(mValue.getAuthenticationData());
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
