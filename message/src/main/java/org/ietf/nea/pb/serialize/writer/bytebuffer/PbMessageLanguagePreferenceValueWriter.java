package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PbMessageLanguagePreferenceValueWriter implements TnccsWriter<PbMessageValueLanguagePreference>{

	@Override
	public void write(final PbMessageValueLanguagePreference data, final ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message value cannot be NULL.");
		}
		
		PbMessageValueLanguagePreference mValue = data;

		try{
			
			/* language preference */
			buffer.write(mValue.getPreferedLanguage().getBytes(Charset.forName("US-ASCII")));
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}
}
