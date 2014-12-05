package org.ietf.nea.pb.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.PbMessageValueExperimental;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsWriter;

class PbMessageExperimentalValueWriter implements TnccsWriter<PbMessageValueExperimental>{

	@Override
	public void write(final PbMessageValueExperimental data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message value cannot be NULL.");
		}
		
		PbMessageValueExperimental mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* message */
		try{
			buffer.write(mValue.getMessage().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Message could not be written to the buffer.", e, false,
					mValue.getMessage());
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
