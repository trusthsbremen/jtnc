package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterString;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;

class PaAttributeRemediationParameterStringValueWriter implements ImWriter<PaAttributeValueRemediationParameterString>{

	@Override
	public void write(final PaAttributeValueRemediationParameterString data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
		PaAttributeValueRemediationParameterString mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* String length 32 bit(s)*/
		byte[] length = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(mValue.getStringLength()).array(), 4,8);
		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException(
					"Remediation length could not be written to the buffer.", e, false,
					Long.toString(mValue.getStringLength()));
		}
		
		/* reason String*/
		try{
			buffer.write(mValue.getRemediationString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"remediation could not be written to the buffer.", e, false,
					mValue.getRemediationString());
		}
		
		/* language code length 8 bit(s)*/
		buffer.write(mValue.getLangCodeLength());
		
		/* language code */
		try{
			buffer.write(mValue.getLangCode().getBytes(Charset.forName("US-ASCII")));
		} catch (IOException e) {
			throw new SerializationException(
					"Language could not be written to the buffer.", e, false,
					mValue.getLangCode());
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
