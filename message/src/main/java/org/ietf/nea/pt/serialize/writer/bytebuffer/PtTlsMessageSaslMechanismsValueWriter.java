package org.ietf.nea.pt.serialize.writer.bytebuffer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;
import java.util.List;

import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanisms;
import org.ietf.nea.pt.value.util.SaslMechanism;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.bytebuffer.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageSaslMechanismsValueWriter implements TransportWriter<PtTlsMessageValueSaslMechanisms>{

	@Override
	public void write(final PtTlsMessageValueSaslMechanisms data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		if(buffer == null){
			throw new NullPointerException("Buffer cannot be NULL.");
		}
		
		PtTlsMessageValueSaslMechanisms mValue = data;

		try{
	
			/* mechanisms (variable length)*/
			List<SaslMechanism> mechs = mValue.getMechanisms();
			
			if(mechs != null){
				for (SaslMechanism mech : mechs) {
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
