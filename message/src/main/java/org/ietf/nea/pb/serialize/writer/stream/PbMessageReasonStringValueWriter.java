package org.ietf.nea.pb.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.ietf.nea.pb.message.PbMessageValueReasonString;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsWriter;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageReasonStringValueWriter implements TnccsWriter<PbMessageValueReasonString>{

	@Override
	public void write(final PbMessageValueReasonString data, final OutputStream out)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueReasonString mValue = data;
		
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
			buffer.write(mValue.getReasonString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Reason could not be written to the buffer.", e, false,
					mValue.getReasonString());
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
