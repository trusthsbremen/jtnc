package org.ietf.nea.pt.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;

import org.ietf.nea.pt.value.PtTlsMessageValueError;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageErrorValueWriter implements TransportWriter<PtTlsMessageValueError>{

	@Override
	public void write(final PtTlsMessageValueError data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}

		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PtTlsMessageValueError mValue = data;

		try{
			
			/* reserved 8 bit(s) */
			buffer.writeByte((byte)0);
			
			/* vendor ID 24 bit(s) */
			buffer.writeDigits(mValue.getErrorVendorId(), (byte)3);


			/* message Type 32 bit(s) */
			buffer.writeUnsignedInt(mValue.getErrorCode());

			/* message copy max 1024 byte(s) */
			if(mValue.getPartialMessageCopy() != null){
				buffer.write(mValue.getPartialMessageCopy());
			}
			
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
