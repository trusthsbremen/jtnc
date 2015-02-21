package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterString;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageRemediationParameterStringSubValueWriter implements TnccsWriter<PbMessageValueRemediationParameterString>{

	@Override
	public void write(final PbMessageValueRemediationParameterString data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueRemediationParameterString mValue = data;
		
		
		try{
			/* String length 32 bit(s)*/
			buffer.writeUnsignedInt(mValue.getStringLength());
			
			/* reason String*/
			buffer.write(mValue.getRemediationString().getBytes(Charset.forName("UTF-8")));
			
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
