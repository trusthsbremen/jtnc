package org.ietf.nea.pt.serialize.writer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pt.message.PtTlsMessageHeader;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageHeaderWriter implements TransportWriter<PtTlsMessageHeader>{

	@Override
	public void write(final PtTlsMessageHeader data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PtTlsMessageHeader mHead = data;

		try{
			
			/* reserved 8 bit(s) */
			buffer.writeByte((byte)0);
			
			/* vendor ID 24 bit(s) */
			buffer.writeDigits(mHead.getVendorId(), (byte)3);


			/* message Type 32 bit(s) */
			buffer.writeUnsignedInt(mHead.getMessageType());

			/* message length 32 bit(s) */
			buffer.writeUnsignedInt(mHead.getLength());


			/* message identifier 32 bit(s) */
			buffer.writeUnsignedInt(mHead.getIdentifier());

		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
