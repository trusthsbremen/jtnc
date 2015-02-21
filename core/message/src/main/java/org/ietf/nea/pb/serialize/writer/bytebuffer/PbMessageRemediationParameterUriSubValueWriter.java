package org.ietf.nea.pb.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pb.message.util.PbMessageValueRemediationParameterUri;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.bytebuffer.TnccsWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PbMessageRemediationParameterUriSubValueWriter implements TnccsWriter<PbMessageValueRemediationParameterUri>{

	@Override
	public void write(final PbMessageValueRemediationParameterUri data, final ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message value cannot be null.", data);
		
		PbMessageValueRemediationParameterUri mValue = data;
		
		try{
			/* URI */
			buffer.write(mValue.getRemediationUri().toString().getBytes(Charset.forName("UTF-8")));
			
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
	}
}
