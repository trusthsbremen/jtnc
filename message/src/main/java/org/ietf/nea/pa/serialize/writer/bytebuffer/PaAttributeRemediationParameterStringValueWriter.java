package org.ietf.nea.pa.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterString;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.bytebuffer.ImWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PaAttributeRemediationParameterStringValueWriter implements ImWriter<PaAttributeValueRemediationParameterString>{

	@Override
	public void write(final PaAttributeValueRemediationParameterString data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PaAttributeValueRemediationParameterString mValue = data;
		
		try{

			/* String length 32 bit(s)*/
			buffer.writeUnsignedInt(mValue.getStringLength());
			
			/* reason String*/
			buffer.write(mValue.getRemediationString().getBytes(Charset.forName("UTF-8")));
		
			/* language code length 8 bit(s)*/
			buffer.writeUnsignedShort(mValue.getLangCodeLength());
			
			/* language code */
			buffer.write(mValue.getLangCode().getBytes(Charset.forName("US-ASCII")));

		}catch(BufferOverflowException e){
			throw new SerializationException(
				"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
				Long.toString(buffer.capacity()));
		}
	}

}
