package org.ietf.nea.pa.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.stream.ImWriter;
import de.hsbremen.tc.tnc.util.NotNull;

class PaAttributeRemediationParameterUriValueWriter implements ImWriter<PaAttributeValueRemediationParameterUri>{

	@Override
	public void write(final PaAttributeValueRemediationParameterUri data, final OutputStream out)
			throws SerializationException {
		NotNull.check("Value cannot be null.", data);
		
		PaAttributeValueRemediationParameterUri mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* URI */
		try{
			buffer.write(mValue.getRemediationUri().toString().getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			throw new SerializationException(
					"Uri could not be written to the buffer.", e, false,
					mValue.getRemediationUri().toString());
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Attribute could not be written to the OutputStream.",e, true);
		}
	}

}
