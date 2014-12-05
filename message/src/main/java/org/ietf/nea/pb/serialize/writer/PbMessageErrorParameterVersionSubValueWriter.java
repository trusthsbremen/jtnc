package org.ietf.nea.pb.serialize.writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterVersion;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.TnccsWriter;

class PbMessageErrorParameterVersionSubValueWriter implements TnccsWriter<PbMessageValueErrorParameterVersion>{

	private static final byte RESERVED = 0;
	
	@Override
	public void write(final PbMessageValueErrorParameterVersion data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message value cannot be NULL.");
		}
		
		PbMessageValueErrorParameterVersion mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* bad version */
		buffer.write(mValue.getBadVersion());
		
		/* max version */
		buffer.write(mValue.getMaxVersion());
		
		/* min version */
		buffer.write(mValue.getMinVersion());
		
		/* reserved */
		buffer.write(RESERVED);

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
