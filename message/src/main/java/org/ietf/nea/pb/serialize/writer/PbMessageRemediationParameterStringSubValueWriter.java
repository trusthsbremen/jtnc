package org.ietf.nea.pb.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueRemediationParameterString;

import de.hsbremen.tc.tnc.tnccs.exception.SerializationException;
import de.hsbremen.tc.tnc.tnccs.serialize.TnccsWriter;

class PbMessageRemediationParameterStringSubValueWriter implements TnccsWriter<PbMessageValueRemediationParameterString>{

	@Override
	public void write(final PbMessageValueRemediationParameterString data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PbMessageValueRemediationParameterString mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* String length 32 bit(s)*/
		byte[] length = Arrays.copyOfRange(ByteBuffer.allocate(8).putLong(mValue.getStringLength()).array(), 4,8);
		try {
			buffer.write(length);
		} catch (IOException e) {
			throw new SerializationException(
					"Message length could not be written to the buffer.", e, false,
					Long.toString(mValue.getStringLength()));
		}
		
		/* reason String*/
		try{
			buffer.write(mValue.getRemediationString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Reason could not be written to the buffer.", e, false,
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
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
