package org.ietf.nea.pt.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelection;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PtTlsMessageSaslMechanismSelectionValueWriter implements TransportWriter<PtTlsMessageValueSaslMechanismSelection>{

	@Override
	public void write(final PtTlsMessageValueSaslMechanismSelection data, ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message header cannot be null.", data);
		
		NotNull.check("Buffer cannot be null.", buffer);
		
		PtTlsMessageValueSaslMechanismSelection mValue = data;

		try{
	
			/* mechanism selection (variable length)*/
			SaslMechanismEntry mech = mValue.getMechanism();

			/* reserved + name length 8 bit(s) */
			buffer.writeByte(mech.getNameLength());
					
			/* name */
			buffer.write(mech.getName().getBytes((Charset.forName("US-ASCII"))));
			
			if(mValue.getInitialSaslMsg() != null){
				buffer.write(mValue.getInitialSaslMsg());
			}
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
