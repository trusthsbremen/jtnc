package org.ietf.nea.pb.serialize.writer.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.ietf.nea.pb.message.util.PbMessageValueErrorParameterOffset;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.tnccs.serialize.stream.TnccsWriter;

class PbMessageErrorParameterOffsetSubValueWriter implements TnccsWriter<PbMessageValueErrorParameterOffset>{

	@Override
	public void write(final PbMessageValueErrorParameterOffset data, final OutputStream out)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message value cannot be NULL.");
		}
		
		PbMessageValueErrorParameterOffset mValue = data;
		
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		/* Offset */
		try{
			byte[] offset = Arrays.copyOfRange(
					ByteBuffer.allocate(8).putLong(mValue.getOffset()).array(), 4,
					8);
			buffer.write(offset);
		} catch (IOException e) {
			throw new SerializationException(
					"Offset could not be written to the buffer.", e, false,
					Long.toString(mValue.getOffset()));
		}

		try {
			buffer.writeTo(out);
		} catch (IOException e) {
			throw new SerializationException("Message could not be written to the OutputStream.",e, true);
		}
	}

}
