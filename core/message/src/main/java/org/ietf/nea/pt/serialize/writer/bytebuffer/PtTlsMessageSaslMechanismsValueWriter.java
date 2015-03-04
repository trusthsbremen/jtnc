package org.ietf.nea.pt.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;
import java.util.List;

import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanisms;
import org.ietf.nea.pt.value.util.SaslMechanismEntry;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;
import de.hsbremen.tc.tnc.util.NotNull;

class PtTlsMessageSaslMechanismsValueWriter implements TransportWriter<PtTlsMessageValueSaslMechanisms>{

	@Override
	public void write(final PtTlsMessageValueSaslMechanisms data, ByteBuffer buffer)
			throws SerializationException {
		NotNull.check("Message header cannot be null.", data);
		
		NotNull.check("Buffer cannot be null.", buffer);
		
		PtTlsMessageValueSaslMechanisms mValue = data;

		try{
	
			/* mechanisms (variable length)*/
			List<SaslMechanismEntry> mechs = mValue.getMechanisms();
			
			if(mechs != null){
				for (SaslMechanismEntry mech : mechs) {
					/* reserved + name length 8 bit(s) */
					buffer.writeByte(mech.getNameLength());
					
					/* name */
					buffer.write(mech.getName().getBytes((Charset.forName("US-ASCII"))));
				}
			}
		
		}catch(BufferOverflowException e){
			throw new SerializationException(
					"Buffer capacity "+ buffer.capacity() + " to short.", e, false,
					Long.toString(buffer.capacity()));
		}
		
	}

}
