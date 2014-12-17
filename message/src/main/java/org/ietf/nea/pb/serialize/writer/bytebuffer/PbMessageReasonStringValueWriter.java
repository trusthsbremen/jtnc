package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.PbMessageValueReasonString;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PbMessageReasonStringValueWriter implements TnccsWriter<PbMessageValueReasonString>{

	@Override
	public void write(final PbMessageValueReasonString data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message value cannot be NULL.");
		}
		
		PbMessageValueReasonString mValue = data;
		
		try{
			/* String length 32 bit(s)*/
			buffer.writeUnsignedInt(mValue.getStringLength());
			
			/* reason String*/
			buffer.write(mValue.getReasonString().getBytes(Charset.forName("UTF-8")));
			
			
			/* language code length 8 bit(s)*/
			buffer.writeUnsignedByte(mValue.getLangCodeLength());
			
			/* language code */
			buffer.write(mValue.getLangCode().getBytes(Charset.forName("US-ASCII")));
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}

}
