package org.ietf.nea.pb.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.PbMessageValueLanguagePreference;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsWriter;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageLanguagePreferenceValueWriter implements TnccsWriter<PbMessageValueLanguagePreference>{

	@Override
	public void write(final PbMessageValueLanguagePreference data, final OutputStream out)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueLanguagePreference mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* message */
		try{
			buffer.write(mValue.getPreferedLanguage().getBytes(Charset.forName("US-ASCII")));
		} catch (IOException e) {
			throw new SerializationException(
					"Message could not be written to the buffer.", e, false,
					mValue.getPreferedLanguage());
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
