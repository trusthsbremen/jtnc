package org.ietf.nea.pt.serialize.writer;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;

import org.ietf.nea.pt.value.PtTlsMessageValueSaslMechanismSelection;
import org.ietf.nea.pt.value.util.SaslMechanism;

import de.hsbremen.tc.tnc.message.exception.SerializationException;
import de.hsbremen.tc.tnc.message.t.serialize.TransportWriter;
import de.hsbremen.tc.tnc.message.util.ByteBuffer;

class PtTlsMessageSaslMechanismSelectionValueWriter implements TransportWriter<PtTlsMessageValueSaslMechanismSelection>{

	@Override
	public void write(final PtTlsMessageValueSaslMechanismSelection data, ByteBuffer buffer)
			throws SerializationException {
		if(data == null){
			throw new NullPointerException("Message header cannot be NULL.");
		}
		
		PtTlsMessageValueSaslMechanismSelection mValue = data;

		try{
	
			/* mechanism selection (variable length)*/
			SaslMechanism mech = mValue.getMechanism();

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
