package org.ietf.nea.pb.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsWriter;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageRemediationParameterUriSubValueWriter implements TnccsWriter<PbMessageValueRemediationParameterUri>{

	@Override
	public void write(final PbMessageValueRemediationParameterUri data, final OutputStream out)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueRemediationParameterUri mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* URI */
		try{
			buffer.write(mValue.getRemediationUri().toString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Message could not be written to the buffer.", e, false,
					mValue.getRemediationUri().toString());
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
