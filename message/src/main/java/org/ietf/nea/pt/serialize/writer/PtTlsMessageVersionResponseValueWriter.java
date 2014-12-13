package org.ietf.nea.pt.serialize.writer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pt.value.PtTlsMessageValueVersionResponse;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageVersionResponseValueWriter implements TransportWriter<PtTlsMessageValueVersionResponse>{

	@Override
	public void write(final PtTlsMessageValueVersionResponse data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PtTlsMessageValueVersionResponse mValue = data;

		try{
	
			/* reserved 24 bit(s) */
			buffer.writeDigits(0, (byte)3);
			
			/* preferred version 8 bit(s) */
			buffer.writeUnsignedByte(mValue.getSelectedVersion());
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
