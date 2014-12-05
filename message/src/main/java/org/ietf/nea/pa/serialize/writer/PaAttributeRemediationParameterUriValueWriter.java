package org.ietf.nea.pa.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.ietf.nea.pa.attribute.util.PaAttributeValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.m.serialize.ImWriter;

class PaAttributeRemediationParameterUriValueWriter implements ImWriter<PaAttributeValueRemediationParameterUri>{

	@Override
	public void write(final PaAttributeValueRemediationParameterUri data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Value cannot be NULL.");
		}
		
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
